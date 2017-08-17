package com.creativemonkeyz.robotzi;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

public class Settings extends SherlockPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String NOTIF_TRUE = "Activat";
    private static final String NOTIF_FALSE = "Dezactivat";

    private static final String DOWNLOAD_DIR_EP = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/RObotzi";
    private static final String DOWNLOAD_DIR_WALL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/RObotzi";

    private static final String BUTTON_OK = "OK";
    private static final String BUTTON_CANCEL = "Anulare";
    private static final String BUTTON_NEW_FOLDER = "Folder nou";

    private String keys[] = new String[] {"NOTIF_EP", "NOTIF_WL", "DL_EP", "DL_WL"};

    private SharedPreferences settingsPref;
	private SharedPreferences.Editor settingsPrefEditor;

	private String[] mFileList;
	private File mPath;
	private int dialogId;

	private String key;
	private String defaultDir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		addPreferencesFromResource(R.xml.settings);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		settingsPref = PreferenceManager.getDefaultSharedPreferences(this);
		settingsPrefEditor = settingsPref.edit();

        for (String key : keys)
            getPreferenceScreen().findPreference(key).setSummary(settingsPref.getBoolean(key, false) ? NOTIF_TRUE : NOTIF_FALSE);

		Preference downloadDir = getPreferenceScreen().findPreference("DIR_EP");
		downloadDir.setSummary(settingsPref.getString("DIR_EP", DOWNLOAD_DIR_EP));

		downloadDir.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference paramAnonymousPreference) {
				key = "DIR_EP";
				defaultDir = DOWNLOAD_DIR_EP;
				mPath = new File(settingsPref.getString("DIR_EP", defaultDir));

				Settings.this.showDialog(dialogId);
				return true;
			}
		});

		Preference downloadDirWalls = getPreferenceScreen().findPreference("DIR_WL");
		downloadDirWalls.setSummary(settingsPref.getString("DIR_WL", DOWNLOAD_DIR_WALL));

		downloadDirWalls.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference paramAnonymousPreference) {
                key = "DIR_WL";
                defaultDir = DOWNLOAD_DIR_WALL;
                mPath = new File(settingsPref.getString("DIR_WL", defaultDir));

                Settings.this.showDialog(dialogId);
                return true;
            }
        });
	}

	protected Dialog onCreateDialog(int id) {
		dialogId++;

		String[] tempFileList = new String[0];
		if (mPath.exists()) {
			tempFileList = mPath.list(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					return sel.isDirectory();
				}
			});

			Arrays.sort(tempFileList, new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					return lhs.compareToIgnoreCase(rhs);
				}
			});
		}

		mFileList = new String[tempFileList.length + 1];
		mFileList[0] = "..";

        System.arraycopy(tempFileList, 0, mFileList, 1, tempFileList.length);

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(mPath.getPath());

		if (mFileList == null)
			return builder.create();

		builder.setItems(mFileList, new DialogInterface.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(DialogInterface dialog, int which) {
				String chosenDir = mFileList[which];

				if (chosenDir.equals(".."))
					mPath = mPath.getAbsoluteFile().getParentFile() != null ? mPath.getAbsoluteFile().getParentFile() : mPath;
				else
					mPath = new File(mPath.getPath() + "/" + chosenDir);

				Settings.this.showDialog(dialogId);
			}
		});

		builder.setPositiveButton(BUTTON_OK, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
                if ((mPath.mkdirs() || mPath.exists()) && mPath.canWrite()) {
                    settingsPrefEditor.putString(key, mPath.getPath()).commit();
                    getPreferenceScreen().findPreference(key).setSummary(mPath.getPath());

                } else {
                    settingsPrefEditor.putString(key, defaultDir).commit();
                    Utils.showToast(Settings.this.getString(R.string.error_invalid_dir), Settings.this, Style.ALERT);
                    mPath = new File(defaultDir);
                }
			}
		});

		builder.setNeutralButton(BUTTON_NEW_FOLDER, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final View view = LayoutInflater.from(Settings.this).inflate(R.layout.new_folder, null);
				new AlertDialog.Builder(Settings.this).setTitle(BUTTON_NEW_FOLDER + ":").setView(view)
						.setPositiveButton(BUTTON_OK, new DialogInterface.OnClickListener() {
							@SuppressWarnings("deprecation")
							public void onClick(DialogInterface dialog, int whichButton) {
								File tempPath = new File(mPath.getPath() + '/' + ((EditText) view.findViewById(R.id.editText)).getText());

								if (!(tempPath.mkdirs() || tempPath.isDirectory() || tempPath.canWrite()))
									Utils.showToast(Settings.this.getString(R.string.error_general), Settings.this, Style.ALERT);

								Settings.this.showDialog(dialogId);
							}
						}).setNegativeButton(BUTTON_CANCEL, new DialogInterface.OnClickListener() {
							@SuppressWarnings("deprecation")
							public void onClick(DialogInterface dialog, int whichButton) {
								Settings.this.showDialog(dialogId);
							}
						}).create().show();
			}
		});

		builder.setNegativeButton(BUTTON_CANCEL, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {}
		});

		return builder.show();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences settingsPref, String key) {
        getPreferenceScreen().findPreference(key).setSummary(settingsPref.getBoolean(key, false) ? NOTIF_TRUE : NOTIF_FALSE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}