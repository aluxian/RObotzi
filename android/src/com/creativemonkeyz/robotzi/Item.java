package com.creativemonkeyz.robotzi;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
    public int id;
    public String name, episode, season, fileId, youtubeLink, downloadLink, previewLink;

    public Item(JSONObject object) {
        try {
            this.id = Integer.parseInt(object.getString("objectId"));
            this.name = object.getString("name");
            this.previewLink = object.getString("preview");
        } catch (JSONException ignored) {}

        try {
            this.episode = name.substring(4);
            this.season = name.substring(0, 3);
            this.youtubeLink = "http://www.youtube.com/watch?v=" + object.getString("youtube");
            this.fileId = object.getString("file_id");
        } catch (JSONException ignored) {}

        try {
            this.downloadLink = object.getString("download");
        } catch (JSONException ignored) {}
    }
}
