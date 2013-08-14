package org.hk.training.toilet.provider;

import android.content.Context;
import android.database.Cursor;

import org.hk.training.toilet.pojo.Training;
import org.hk.training.toilet.util.Lists;

import java.util.Calendar;
import java.util.List;

import static org.hk.training.toilet.provider.TrainingProvider.TrainingEnum.*;
import static org.hk.training.toilet.provider.DatabaseHelper.ColumnType.*;
import static org.hk.training.toilet.provider.DatabaseHelper.createTableFormat;
import static org.hk.training.toilet.provider.DatabaseHelper.dropTableFormat;
import static org.hk.training.toilet.provider.DatabaseHelper.format;

public class TrainingProvider extends AbstractProvider<Training, Training> {

    public static final String _TABLE_NAME = "training";

    public enum TrainingEnum {
        id,
        image_path,
        comment,
        success,
        is_large,
        created_date,
        deleted,
        created_at,
        updated_at
    }

    private static final String[] COLUMNS = {
            id.name(),
            image_path.name(),
            comment.name(),
            success.name(),
            is_large.name(),
            created_date.name(),
            deleted.name(),
            created_at.name(),
            updated_at.name()};

    public TrainingProvider(Context context) {
        super(context);
    }

    @Override
    String tableName() {
        return _TABLE_NAME;
    }

    @Override
    String[] columns() {
        return COLUMNS;
    }

    public static String create() {
        return createTableFormat(
                _TABLE_NAME,
                format(id.name(), INTEGER_PRIMARY_KEY_NOT_NULL),
                format(image_path.name(), TEXT),
                format(comment.name(), TEXT),
                format(success.name(), INT),
                format(is_large.name(), INT),
                format(created_date.name(), LONG),
                format(deleted.name(), INT),
                format(created_at.name(), LONG),
                format(updated_at.name(), LONG)
        );
    }

    public static List<String> upgrade(final int oldVersion, final int newVersion) {
        List<String> sql = Lists.newEmptyArrayList();
        sql.add(dropTableFormat(_TABLE_NAME));
        sql.add(create());
        return sql;
    }

    @Override
    Training createResult(Cursor cur, String[] columns) {
        Training t = new Training();
        t.id = cur.getInt(cur.getColumnIndex(id.name()));
        t.imagePath = cur.getString(cur.getColumnIndex(image_path.name()));
        t.comment = cur.getString(cur.getColumnIndex(comment.name()));
        t.success = cur.getInt(cur.getColumnIndex(success.name())) == 1;
        t.isLarge = cur.getInt(cur.getColumnIndex(is_large.name())) == 1;
        t.createdDate = toCalendar(cur.getLong(cur.getColumnIndex(created_date.name())));
        t.deleted = cur.getInt(cur.getColumnIndex(deleted.name())) == 1;
        t.createdAt = toCalendar(cur.getLong(cur.getColumnIndex(created_at.name())));
        t.updatedAt = toCalendar(cur.getLong(cur.getColumnIndex(updated_at.name())));
        return t;
    }

    private Calendar toCalendar(long cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cal);
        return calendar;
    }
}
