package com.example.callguardian.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.callguardian.model.BlockMode;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class PhoneProfileDao_Impl implements PhoneProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PhoneProfileEntity> __insertionAdapterOfPhoneProfileEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<PhoneProfileEntity> __updateAdapterOfPhoneProfileEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBlockMode;

  private final SharedSQLiteStatement __preparedStmtOfUpdateContactInfo;

  public PhoneProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPhoneProfileEntity = new EntityInsertionAdapter<PhoneProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `phone_profiles` (`phone_number`,`display_name`,`carrier`,`region`,`line_type`,`last_lookup_at`,`spam_score`,`tags`,`block_mode`,`notes`,`contact_id`,`is_existing_contact`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhoneProfileEntity entity) {
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPhoneNumber());
        }
        if (entity.getDisplayName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDisplayName());
        }
        if (entity.getCarrier() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCarrier());
        }
        if (entity.getRegion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getRegion());
        }
        if (entity.getLineType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLineType());
        }
        final Long _tmp = __converters.toEpoch(entity.getLastLookupAt());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp);
        }
        if (entity.getSpamScore() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getSpamScore());
        }
        final String _tmp_1 = __converters.toTags(entity.getTags());
        if (_tmp_1 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_1);
        }
        final String _tmp_2 = __converters.toBlockMode(entity.getBlockMode());
        if (_tmp_2 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_2);
        }
        if (entity.getNotes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNotes());
        }
        if (entity.getContactId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getContactId());
        }
        final int _tmp_3 = entity.isExistingContact() ? 1 : 0;
        statement.bindLong(12, _tmp_3);
      }
    };
    this.__updateAdapterOfPhoneProfileEntity = new EntityDeletionOrUpdateAdapter<PhoneProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `phone_profiles` SET `phone_number` = ?,`display_name` = ?,`carrier` = ?,`region` = ?,`line_type` = ?,`last_lookup_at` = ?,`spam_score` = ?,`tags` = ?,`block_mode` = ?,`notes` = ?,`contact_id` = ?,`is_existing_contact` = ? WHERE `phone_number` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhoneProfileEntity entity) {
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPhoneNumber());
        }
        if (entity.getDisplayName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDisplayName());
        }
        if (entity.getCarrier() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCarrier());
        }
        if (entity.getRegion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getRegion());
        }
        if (entity.getLineType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLineType());
        }
        final Long _tmp = __converters.toEpoch(entity.getLastLookupAt());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp);
        }
        if (entity.getSpamScore() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getSpamScore());
        }
        final String _tmp_1 = __converters.toTags(entity.getTags());
        if (_tmp_1 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_1);
        }
        final String _tmp_2 = __converters.toBlockMode(entity.getBlockMode());
        if (_tmp_2 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_2);
        }
        if (entity.getNotes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNotes());
        }
        if (entity.getContactId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getContactId());
        }
        final int _tmp_3 = entity.isExistingContact() ? 1 : 0;
        statement.bindLong(12, _tmp_3);
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getPhoneNumber());
        }
      }
    };
    this.__preparedStmtOfUpdateBlockMode = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE phone_profiles SET block_mode = ? WHERE phone_number = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateContactInfo = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE phone_profiles SET contact_id = ?, is_existing_contact = ? WHERE phone_number = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final PhoneProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPhoneProfileEntity.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PhoneProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPhoneProfileEntity.handle(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBlockMode(final String phoneNumber, final String blockMode,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBlockMode.acquire();
        int _argIndex = 1;
        if (blockMode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, blockMode);
        }
        _argIndex = 2;
        if (phoneNumber == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, phoneNumber);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateBlockMode.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateContactInfo(final String phoneNumber, final Long contactId,
      final boolean isExistingContact, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateContactInfo.acquire();
        int _argIndex = 1;
        if (contactId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, contactId);
        }
        _argIndex = 2;
        final int _tmp = isExistingContact ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 3;
        if (phoneNumber == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, phoneNumber);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateContactInfo.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getByNumber(final String phoneNumber,
      final Continuation<? super PhoneProfileEntity> $completion) {
    final String _sql = "SELECT * FROM phone_profiles WHERE phone_number = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (phoneNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, phoneNumber);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PhoneProfileEntity>() {
      @Override
      @Nullable
      public PhoneProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "display_name");
          final int _cursorIndexOfCarrier = CursorUtil.getColumnIndexOrThrow(_cursor, "carrier");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfLineType = CursorUtil.getColumnIndexOrThrow(_cursor, "line_type");
          final int _cursorIndexOfLastLookupAt = CursorUtil.getColumnIndexOrThrow(_cursor, "last_lookup_at");
          final int _cursorIndexOfSpamScore = CursorUtil.getColumnIndexOrThrow(_cursor, "spam_score");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfBlockMode = CursorUtil.getColumnIndexOrThrow(_cursor, "block_mode");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfIsExistingContact = CursorUtil.getColumnIndexOrThrow(_cursor, "is_existing_contact");
          final PhoneProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpDisplayName;
            if (_cursor.isNull(_cursorIndexOfDisplayName)) {
              _tmpDisplayName = null;
            } else {
              _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            }
            final String _tmpCarrier;
            if (_cursor.isNull(_cursorIndexOfCarrier)) {
              _tmpCarrier = null;
            } else {
              _tmpCarrier = _cursor.getString(_cursorIndexOfCarrier);
            }
            final String _tmpRegion;
            if (_cursor.isNull(_cursorIndexOfRegion)) {
              _tmpRegion = null;
            } else {
              _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
            }
            final String _tmpLineType;
            if (_cursor.isNull(_cursorIndexOfLineType)) {
              _tmpLineType = null;
            } else {
              _tmpLineType = _cursor.getString(_cursorIndexOfLineType);
            }
            final Instant _tmpLastLookupAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastLookupAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastLookupAt);
            }
            _tmpLastLookupAt = __converters.fromEpoch(_tmp);
            final Integer _tmpSpamScore;
            if (_cursor.isNull(_cursorIndexOfSpamScore)) {
              _tmpSpamScore = null;
            } else {
              _tmpSpamScore = _cursor.getInt(_cursorIndexOfSpamScore);
            }
            final List<String> _tmpTags;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfTags);
            }
            _tmpTags = __converters.fromTags(_tmp_1);
            final BlockMode _tmpBlockMode;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfBlockMode)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfBlockMode);
            }
            _tmpBlockMode = __converters.fromBlockMode(_tmp_2);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final boolean _tmpIsExistingContact;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsExistingContact);
            _tmpIsExistingContact = _tmp_3 != 0;
            _result = new PhoneProfileEntity(_tmpPhoneNumber,_tmpDisplayName,_tmpCarrier,_tmpRegion,_tmpLineType,_tmpLastLookupAt,_tmpSpamScore,_tmpTags,_tmpBlockMode,_tmpNotes,_tmpContactId,_tmpIsExistingContact);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<PhoneProfileEntity> observeByNumber(final String phoneNumber) {
    final String _sql = "SELECT * FROM phone_profiles WHERE phone_number = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (phoneNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, phoneNumber);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"phone_profiles"}, new Callable<PhoneProfileEntity>() {
      @Override
      @Nullable
      public PhoneProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "display_name");
          final int _cursorIndexOfCarrier = CursorUtil.getColumnIndexOrThrow(_cursor, "carrier");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfLineType = CursorUtil.getColumnIndexOrThrow(_cursor, "line_type");
          final int _cursorIndexOfLastLookupAt = CursorUtil.getColumnIndexOrThrow(_cursor, "last_lookup_at");
          final int _cursorIndexOfSpamScore = CursorUtil.getColumnIndexOrThrow(_cursor, "spam_score");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfBlockMode = CursorUtil.getColumnIndexOrThrow(_cursor, "block_mode");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfIsExistingContact = CursorUtil.getColumnIndexOrThrow(_cursor, "is_existing_contact");
          final PhoneProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpDisplayName;
            if (_cursor.isNull(_cursorIndexOfDisplayName)) {
              _tmpDisplayName = null;
            } else {
              _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            }
            final String _tmpCarrier;
            if (_cursor.isNull(_cursorIndexOfCarrier)) {
              _tmpCarrier = null;
            } else {
              _tmpCarrier = _cursor.getString(_cursorIndexOfCarrier);
            }
            final String _tmpRegion;
            if (_cursor.isNull(_cursorIndexOfRegion)) {
              _tmpRegion = null;
            } else {
              _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
            }
            final String _tmpLineType;
            if (_cursor.isNull(_cursorIndexOfLineType)) {
              _tmpLineType = null;
            } else {
              _tmpLineType = _cursor.getString(_cursorIndexOfLineType);
            }
            final Instant _tmpLastLookupAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastLookupAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastLookupAt);
            }
            _tmpLastLookupAt = __converters.fromEpoch(_tmp);
            final Integer _tmpSpamScore;
            if (_cursor.isNull(_cursorIndexOfSpamScore)) {
              _tmpSpamScore = null;
            } else {
              _tmpSpamScore = _cursor.getInt(_cursorIndexOfSpamScore);
            }
            final List<String> _tmpTags;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfTags);
            }
            _tmpTags = __converters.fromTags(_tmp_1);
            final BlockMode _tmpBlockMode;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfBlockMode)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfBlockMode);
            }
            _tmpBlockMode = __converters.fromBlockMode(_tmp_2);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final boolean _tmpIsExistingContact;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsExistingContact);
            _tmpIsExistingContact = _tmp_3 != 0;
            _result = new PhoneProfileEntity(_tmpPhoneNumber,_tmpDisplayName,_tmpCarrier,_tmpRegion,_tmpLineType,_tmpLastLookupAt,_tmpSpamScore,_tmpTags,_tmpBlockMode,_tmpNotes,_tmpContactId,_tmpIsExistingContact);
          } else {
            _result = null;
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

  @Override
  public Flow<List<PhoneProfileEntity>> observeAll() {
    final String _sql = "SELECT * FROM phone_profiles ORDER BY last_lookup_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"phone_profiles"}, new Callable<List<PhoneProfileEntity>>() {
      @Override
      @NonNull
      public List<PhoneProfileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "display_name");
          final int _cursorIndexOfCarrier = CursorUtil.getColumnIndexOrThrow(_cursor, "carrier");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfLineType = CursorUtil.getColumnIndexOrThrow(_cursor, "line_type");
          final int _cursorIndexOfLastLookupAt = CursorUtil.getColumnIndexOrThrow(_cursor, "last_lookup_at");
          final int _cursorIndexOfSpamScore = CursorUtil.getColumnIndexOrThrow(_cursor, "spam_score");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfBlockMode = CursorUtil.getColumnIndexOrThrow(_cursor, "block_mode");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfIsExistingContact = CursorUtil.getColumnIndexOrThrow(_cursor, "is_existing_contact");
          final List<PhoneProfileEntity> _result = new ArrayList<PhoneProfileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PhoneProfileEntity _item;
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpDisplayName;
            if (_cursor.isNull(_cursorIndexOfDisplayName)) {
              _tmpDisplayName = null;
            } else {
              _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            }
            final String _tmpCarrier;
            if (_cursor.isNull(_cursorIndexOfCarrier)) {
              _tmpCarrier = null;
            } else {
              _tmpCarrier = _cursor.getString(_cursorIndexOfCarrier);
            }
            final String _tmpRegion;
            if (_cursor.isNull(_cursorIndexOfRegion)) {
              _tmpRegion = null;
            } else {
              _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
            }
            final String _tmpLineType;
            if (_cursor.isNull(_cursorIndexOfLineType)) {
              _tmpLineType = null;
            } else {
              _tmpLineType = _cursor.getString(_cursorIndexOfLineType);
            }
            final Instant _tmpLastLookupAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastLookupAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastLookupAt);
            }
            _tmpLastLookupAt = __converters.fromEpoch(_tmp);
            final Integer _tmpSpamScore;
            if (_cursor.isNull(_cursorIndexOfSpamScore)) {
              _tmpSpamScore = null;
            } else {
              _tmpSpamScore = _cursor.getInt(_cursorIndexOfSpamScore);
            }
            final List<String> _tmpTags;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfTags);
            }
            _tmpTags = __converters.fromTags(_tmp_1);
            final BlockMode _tmpBlockMode;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfBlockMode)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfBlockMode);
            }
            _tmpBlockMode = __converters.fromBlockMode(_tmp_2);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final boolean _tmpIsExistingContact;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsExistingContact);
            _tmpIsExistingContact = _tmp_3 != 0;
            _item = new PhoneProfileEntity(_tmpPhoneNumber,_tmpDisplayName,_tmpCarrier,_tmpRegion,_tmpLineType,_tmpLastLookupAt,_tmpSpamScore,_tmpTags,_tmpBlockMode,_tmpNotes,_tmpContactId,_tmpIsExistingContact);
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
}
