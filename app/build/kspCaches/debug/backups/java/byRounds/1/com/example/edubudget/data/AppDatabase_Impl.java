package com.example.edubudget.data;

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
public final class AppDatabase_Impl extends AppDatabase {
  private volatile CategoryDao _categoryDao;

  private volatile ExpenseDao _expenseDao;

  private volatile BudgetGoalDao _budgetGoalDao;

  private volatile SavingsGoalDao _savingsGoalDao;

  private volatile BadgeDao _badgeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `category` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `expense` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `description` TEXT NOT NULL, `date` TEXT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT NOT NULL, `category` TEXT NOT NULL, `amount` REAL NOT NULL, `imageUri` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `budget_goal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `minAmount` REAL NOT NULL, `maxAmount` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `savings_goal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `targetAmount` REAL NOT NULL, `savedAmount` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `badge` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `emoji` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `earnedDate` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2a5e4d93150115d6999d6044996333b8')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `category`");
        db.execSQL("DROP TABLE IF EXISTS `expense`");
        db.execSQL("DROP TABLE IF EXISTS `budget_goal`");
        db.execSQL("DROP TABLE IF EXISTS `savings_goal`");
        db.execSQL("DROP TABLE IF EXISTS `badge`");
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
        final HashMap<String, TableInfo.Column> _columnsCategory = new HashMap<String, TableInfo.Column>(2);
        _columnsCategory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategory.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategory = new TableInfo("category", _columnsCategory, _foreignKeysCategory, _indicesCategory);
        final TableInfo _existingCategory = TableInfo.read(db, "category");
        if (!_infoCategory.equals(_existingCategory)) {
          return new RoomOpenHelper.ValidationResult(false, "category(com.example.edubudget.data.Category).\n"
                  + " Expected:\n" + _infoCategory + "\n"
                  + " Found:\n" + _existingCategory);
        }
        final HashMap<String, TableInfo.Column> _columnsExpense = new HashMap<String, TableInfo.Column>(8);
        _columnsExpense.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpense.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpense.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpense.put("startTime", new TableInfo.Column("startTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpense.put("endTime", new TableInfo.Column("endTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpense.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpense.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpense.put("imageUri", new TableInfo.Column("imageUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExpense = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExpense = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExpense = new TableInfo("expense", _columnsExpense, _foreignKeysExpense, _indicesExpense);
        final TableInfo _existingExpense = TableInfo.read(db, "expense");
        if (!_infoExpense.equals(_existingExpense)) {
          return new RoomOpenHelper.ValidationResult(false, "expense(com.example.edubudget.data.Expense).\n"
                  + " Expected:\n" + _infoExpense + "\n"
                  + " Found:\n" + _existingExpense);
        }
        final HashMap<String, TableInfo.Column> _columnsBudgetGoal = new HashMap<String, TableInfo.Column>(3);
        _columnsBudgetGoal.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgetGoal.put("minAmount", new TableInfo.Column("minAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgetGoal.put("maxAmount", new TableInfo.Column("maxAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudgetGoal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBudgetGoal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBudgetGoal = new TableInfo("budget_goal", _columnsBudgetGoal, _foreignKeysBudgetGoal, _indicesBudgetGoal);
        final TableInfo _existingBudgetGoal = TableInfo.read(db, "budget_goal");
        if (!_infoBudgetGoal.equals(_existingBudgetGoal)) {
          return new RoomOpenHelper.ValidationResult(false, "budget_goal(com.example.edubudget.data.BudgetGoal).\n"
                  + " Expected:\n" + _infoBudgetGoal + "\n"
                  + " Found:\n" + _existingBudgetGoal);
        }
        final HashMap<String, TableInfo.Column> _columnsSavingsGoal = new HashMap<String, TableInfo.Column>(4);
        _columnsSavingsGoal.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoal.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoal.put("targetAmount", new TableInfo.Column("targetAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoal.put("savedAmount", new TableInfo.Column("savedAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSavingsGoal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSavingsGoal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSavingsGoal = new TableInfo("savings_goal", _columnsSavingsGoal, _foreignKeysSavingsGoal, _indicesSavingsGoal);
        final TableInfo _existingSavingsGoal = TableInfo.read(db, "savings_goal");
        if (!_infoSavingsGoal.equals(_existingSavingsGoal)) {
          return new RoomOpenHelper.ValidationResult(false, "savings_goal(com.example.edubudget.data.SavingsGoal).\n"
                  + " Expected:\n" + _infoSavingsGoal + "\n"
                  + " Found:\n" + _existingSavingsGoal);
        }
        final HashMap<String, TableInfo.Column> _columnsBadge = new HashMap<String, TableInfo.Column>(5);
        _columnsBadge.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBadge.put("emoji", new TableInfo.Column("emoji", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBadge.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBadge.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBadge.put("earnedDate", new TableInfo.Column("earnedDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBadge = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBadge = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBadge = new TableInfo("badge", _columnsBadge, _foreignKeysBadge, _indicesBadge);
        final TableInfo _existingBadge = TableInfo.read(db, "badge");
        if (!_infoBadge.equals(_existingBadge)) {
          return new RoomOpenHelper.ValidationResult(false, "badge(com.example.edubudget.data.Badge).\n"
                  + " Expected:\n" + _infoBadge + "\n"
                  + " Found:\n" + _existingBadge);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2a5e4d93150115d6999d6044996333b8", "339c19ec940ccd4864db95b63bd9bbc1");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "category","expense","budget_goal","savings_goal","badge");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `category`");
      _db.execSQL("DELETE FROM `expense`");
      _db.execSQL("DELETE FROM `budget_goal`");
      _db.execSQL("DELETE FROM `savings_goal`");
      _db.execSQL("DELETE FROM `badge`");
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
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExpenseDao.class, ExpenseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BudgetGoalDao.class, BudgetGoalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SavingsGoalDao.class, SavingsGoalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BadgeDao.class, BadgeDao_Impl.getRequiredConverters());
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
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public ExpenseDao expenseDao() {
    if (_expenseDao != null) {
      return _expenseDao;
    } else {
      synchronized(this) {
        if(_expenseDao == null) {
          _expenseDao = new ExpenseDao_Impl(this);
        }
        return _expenseDao;
      }
    }
  }

  @Override
  public BudgetGoalDao budgetGoalDao() {
    if (_budgetGoalDao != null) {
      return _budgetGoalDao;
    } else {
      synchronized(this) {
        if(_budgetGoalDao == null) {
          _budgetGoalDao = new BudgetGoalDao_Impl(this);
        }
        return _budgetGoalDao;
      }
    }
  }

  @Override
  public SavingsGoalDao savingsGoalDao() {
    if (_savingsGoalDao != null) {
      return _savingsGoalDao;
    } else {
      synchronized(this) {
        if(_savingsGoalDao == null) {
          _savingsGoalDao = new SavingsGoalDao_Impl(this);
        }
        return _savingsGoalDao;
      }
    }
  }

  @Override
  public BadgeDao badgeDao() {
    if (_badgeDao != null) {
      return _badgeDao;
    } else {
      synchronized(this) {
        if(_badgeDao == null) {
          _badgeDao = new BadgeDao_Impl(this);
        }
        return _badgeDao;
      }
    }
  }
}
