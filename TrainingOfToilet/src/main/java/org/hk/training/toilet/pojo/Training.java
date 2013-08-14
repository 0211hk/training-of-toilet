package org.hk.training.toilet.pojo;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import org.hk.training.toilet.provider.DatabaseOperationIf;
import org.hk.training.toilet.provider.TrainingProvider.TrainingEnum;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Training implements JsonParserIf<JSONObject>,DatabaseOperationIf, Cloneable, Parcelable{

    public int id;
    public String imagePath;
    public String comment;
    public boolean success;
    public boolean isLarge;
    public Calendar createdDate;
    public boolean deleted;
    public Calendar createdAt;
    public Calendar updatedAt;

    private Training(final Parcel src){
        parse(src.readString());
    }

    public Training(){

    }

    @Override
    public void parse(JSONObject obj) {
        id = obj.optInt(TrainingEnum.id.name());
        imagePath = obj.optString(TrainingEnum.image_path.name());
        comment = obj.optString(TrainingEnum.comment.name());
        success = obj.optBoolean(TrainingEnum.success.name());
        isLarge = obj.optBoolean(TrainingEnum.is_large.name());
        createdDate = toCalendar(obj.optLong(TrainingEnum.created_date.name()));
        deleted = obj.optBoolean(TrainingEnum.deleted.name());
        createdAt = toCalendar(obj.optLong(TrainingEnum.created_at.name()));
        updatedAt = toCalendar(obj.optLong(TrainingEnum.updated_at.name()));
    }

    private Calendar toCalendar(long cal){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cal);
        return calendar;
    }

    @Override
    public void parse(String str) {
        try {
            parse(new JSONObject(str));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(TrainingEnum.id.name(), id);
        obj.put(TrainingEnum.image_path.name(), imagePath);
        obj.put(TrainingEnum.comment.name(), comment);
        obj.put(TrainingEnum.success.name(), success);
        obj.put(TrainingEnum.is_large.name(), isLarge);
        obj.put(TrainingEnum.created_date.name(), createdDate.getTimeInMillis());
        obj.put(TrainingEnum.deleted.name(), deleted);
        obj.put(TrainingEnum.created_at.name(), createdAt.getTimeInMillis());
        obj.put(TrainingEnum.updated_at.name(), updatedAt.getTimeInMillis());
        return obj;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues c = new ContentValues();

        return null;
    }

    public static final Parcelable.Creator<Training> CREATOR = new Parcelable.Creator<Training>() {
        @Override
        public Training createFromParcel(final Parcel source) {
            return new Training(source);
        }

        @Override
        public Training[] newArray(final int size) {
            return new Training[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeString(toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
