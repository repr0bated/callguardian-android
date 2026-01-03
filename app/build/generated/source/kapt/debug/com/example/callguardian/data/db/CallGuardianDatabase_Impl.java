package com.example.callguardian.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CallGuardianDatabase_Impl extends CallGuardianDatabase {
  private volatile PhoneProfileDao _phoneProfileDao;

  private volatile PhoneInteractionDao _phoneInteractionDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `phone_profiles` (`phone_number` TEXT NOT NULL, `display_name` TEXT, `carrier` TEXT, `region` TEXT, `line_type` TEXT, `last_lookup_at` INTEGER NOT NULL, `spam_score` INTEGER, `tags` TEXT NOT NULL, `block_mode` TEXT NOT NULL, `notes` TEXT, `contact_id` INTEGER, `is_existing_contact` INTEGER NOT NULL, PRIMARY KEY(`phone_number`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `phone_interactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `phone_number` TEXT NOT NULL, `type` TEXT NOT NULL, `direction` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `status` TEXT NOT NULL, `message_body` TEXT, `lookup_summary` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '888901ee794e1ebc581901dbbd6ccf23')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `phone_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `phone_interactions`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPhoneProfiles = new HashMap<String, TableInfo.Column>(12);
        _columnsPhoneProfiles.put("phone_number", new TableInfo.Column("phone_number", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("display_name", new TableInfo.Column("display_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("carrier", new TableInfo.Column("carrier", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("region", new TableInfo.Column("region", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("line_type", new TableInfo.Column("line_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("last_lookup_at", new TableInfo.Column("last_lookup_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("spam_score", new TableInfo.Column("spam_score", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("block_mode", new TableInfo.Column("block_mode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("contact_id", new TableInfo.Column("contact_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneProfiles.put("is_existing_contact", new TableInfo.Column("is_existing_contact", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPhoneProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPhoneProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPhoneProfiles = new TableInfo("phone_profiles", _columnsPhoneProfiles, _foreignKeysPhoneProfiles, _indicesPhoneProfiles);
        final TableInfo _existingPhoneProfiles = TableInfo.read(db, "phone_profiles");
        if (!_infoPhoneProfiles.equals(_existingPhoneProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "phone_profiles(com.example.callguardian.data.db.PhoneProfileEntity).\n"
                  + " Expected:\n" + _infoPhoneProfiles + "\n"
                  + " Found:\n" + _existingPhoneProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsPhoneInteractions = new HashMap<String, TableInfo.Column>(8);
        _columnsPhoneInteractions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneInteractions.put("phone_number", new TableInfo.Column("phone_number", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneInteractions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneInteractions.put("direction", new TableInfo.Column("direction", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneInteractions.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneInteractions.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneInteractions.put("message_body", new TableInfo.Column("message_body", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhoneInteractions.put("lookup_summary", new TableInfo.Column("lookup_summary", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPhoneInteractions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPhoneInteractions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPhoneInteractions = new TableInfo("phone_interactions", _columnsPhoneInteractions, _foreignKeysPhoneInteractions, _indicesPhoneInteractions);
        final TableInfo _existingPhoneInteractions = TableInfo.read(db, "phone_interactions");
        if (!_infoPhoneInteractions.equals(_existingPhoneInteractions)) {
          return new RoomOpenHelper.ValidationResult(false, "phone_interactions(com.example.callguardian.data.db.PhoneInteractionEntity).\n"
                  + " Expected:\n" + _infoPhoneInteractions + "\n"
                  + " Found:\n" + _existingPhoneInteractions);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "888901ee794e1ebc581901dbbd6ccf23", "dab934978fb42bf02ddb3950e6ec8443");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "phone_profiles","phone_interactions");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `phone_profiles`");
      _db.execSQL("DELETE FROM `phone_interactions`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PhoneProfileDao.class, PhoneProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PhoneInteractionDao.class, PhoneInteractionDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PhoneProfileDao phoneProfileDao() {
    if (_phoneProfileDao != null) {
      return _phoneProfileDao;
    } else {
      synchronized(this) {
        if(_phoneProfileDao == null) {
          _phoneProfileDao = new PhoneProfileDao_Impl(this);
        }
        return _phoneProfileDao;
      }
    }
  }

  @Override
  public PhoneInteractionDao phoneInteractionDao() {
    if (_phoneInteractionDao != null) {
      return _phoneInteractionDao;
    } else {
      synchronized(this) {
        if(_phoneInteractionDao == null) {
          _phoneInteractionDao = new PhoneInteractionDao_Impl(this);
        }
        return _phoneInteractionDao;
      }
    }
  }
}
