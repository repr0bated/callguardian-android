package com.example.callguardian.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.callguardian.model.InteractionDirection;
import com.example.callguardian.model.InteractionType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PhoneInteractionDao_Impl implements PhoneInteractionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PhoneInteractionEntity> __insertionAdapterOfPhoneInteractionEntity;

  private final Converters __converters = new Converters();

  public PhoneInteractionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPhoneInteractionEntity = new EntityInsertionAdapter<PhoneInteractionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `phone_interactions` (`id`,`phone_number`,`type`,`direction`,`timestamp`,`status`,`message_body`,`lookup_summary`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhoneInteractionEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPhoneNumber());
        }
        statement.bindString(3, __InteractionType_enumToString(entity.getType()));
        statement.bindString(4, __InteractionDirection_enumToString(entity.getDirection()));
        final Long _tmp = __converters.toEpoch(entity.getTimestamp());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStatus());
        }
        if (entity.getMessageBody() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMessageBody());
        }
        if (entity.getLookupSummary() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLookupSummary());
        }
      }
    };
  }

  @Override
  public Object insert(final PhoneInteractionEntity interaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPhoneInteractionEntity.insert(interaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PhoneInteractionEntity>> observeRecent(final int limit) {
    final String _sql = "SELECT * FROM phone_interactions ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"phone_interactions"}, new Callable<List<PhoneInteractionEntity>>() {
      @Override
      @NonNull
      public List<PhoneInteractionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMessageBody = CursorUtil.getColumnIndexOrThrow(_cursor, "message_body");
          final int _cursorIndexOfLookupSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "lookup_summary");
          final List<PhoneInteractionEntity> _result = new ArrayList<PhoneInteractionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PhoneInteractionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final InteractionType _tmpType;
            _tmpType = __InteractionType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final InteractionDirection _tmpDirection;
            _tmpDirection = __InteractionDirection_stringToEnum(_cursor.getString(_cursorIndexOfDirection));
            final Instant _tmpTimestamp;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            _tmpTimestamp = __converters.fromEpoch(_tmp);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpMessageBody;
            if (_cursor.isNull(_cursorIndexOfMessageBody)) {
              _tmpMessageBody = null;
            } else {
              _tmpMessageBody = _cursor.getString(_cursorIndexOfMessageBody);
            }
            final String _tmpLookupSummary;
            if (_cursor.isNull(_cursorIndexOfLookupSummary)) {
              _tmpLookupSummary = null;
            } else {
              _tmpLookupSummary = _cursor.getString(_cursorIndexOfLookupSummary);
            }
            _item = new PhoneInteractionEntity(_tmpId,_tmpPhoneNumber,_tmpType,_tmpDirection,_tmpTimestamp,_tmpStatus,_tmpMessageBody,_tmpLookupSummary);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __InteractionType_enumToString(@NonNull final InteractionType _value) {
    switch (_value) {
      case CALL: return "CALL";
      case SMS: return "SMS";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __InteractionDirection_enumToString(@NonNull final InteractionDirection _value) {
    switch (_value) {
      case INCOMING: return "INCOMING";
      case OUTGOING: return "OUTGOING";
      case MISSED: return "MISSED";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private InteractionType __InteractionType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "CALL": return InteractionType.CALL;
      case "SMS": return InteractionType.SMS;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private InteractionDirection __InteractionDirection_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "INCOMING": return InteractionDirection.INCOMING;
      case "OUTGOING": return InteractionDirection.OUTGOING;
      case "MISSED": return InteractionDirection.MISSED;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
