package com.wholesale.manager.data.local;

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
import com.wholesale.manager.data.local.dao.BatchDao;
import com.wholesale.manager.data.local.dao.BatchDao_Impl;
import com.wholesale.manager.data.local.dao.ExpenseDao;
import com.wholesale.manager.data.local.dao.ExpenseDao_Impl;
import com.wholesale.manager.data.local.dao.OrderDao;
import com.wholesale.manager.data.local.dao.OrderDao_Impl;
import com.wholesale.manager.data.local.dao.PurchasedRawDao;
import com.wholesale.manager.data.local.dao.PurchasedRawDao_Impl;
import com.wholesale.manager.data.local.dao.UserDao;
import com.wholesale.manager.data.local.dao.UserDao_Impl;
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
  private volatile BatchDao _batchDao;

  private volatile PurchasedRawDao _purchasedRawDao;

  private volatile OrderDao _orderDao;

  private volatile ExpenseDao _expenseDao;

  private volatile UserDao _userDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `batches` (`id` TEXT NOT NULL, `serverId` TEXT, `number` TEXT NOT NULL, `formationDate` TEXT NOT NULL, `purchaseId` TEXT, `rawQuantityKg` REAL NOT NULL, `outputKg` REAL NOT NULL, `outputPercent` INTEGER NOT NULL, `costPrice` REAL NOT NULL, `optimalPricePerKg` REAL, `status` TEXT NOT NULL, `lastModified` TEXT NOT NULL, `isDeleted` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `purchased_raws` (`id` TEXT NOT NULL, `number` INTEGER NOT NULL, `serverId` TEXT, `lastModified` TEXT NOT NULL, `isDeleted` INTEGER NOT NULL, `type` TEXT NOT NULL, `quantityKg` REAL NOT NULL, `purchasePriceTotal` REAL NOT NULL, `pricePerKg` REAL NOT NULL, `supplierName` TEXT NOT NULL, `purchaseDate` TEXT NOT NULL, `status` TEXT NOT NULL, `batchId` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `orders` (`id` TEXT NOT NULL, `serverId` TEXT, `lastModified` TEXT NOT NULL, `isDeleted` INTEGER NOT NULL, `customerName` TEXT NOT NULL, `customerPhone` TEXT NOT NULL, `customerAddress` TEXT, `batchId` TEXT NOT NULL, `quantityKg` REAL NOT NULL, `pricePerKg` REAL NOT NULL, `totalAmount` REAL NOT NULL, `creationDate` TEXT NOT NULL, `shipmentDate` TEXT, `deliveryMethod` TEXT NOT NULL, `status` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `expenses` (`id` TEXT NOT NULL, `serverId` TEXT, `lastModified` TEXT NOT NULL, `isDeleted` INTEGER NOT NULL, `type` TEXT NOT NULL, `amount` REAL NOT NULL, `date` TEXT NOT NULL, `description` TEXT, `batchIdsJson` TEXT NOT NULL, `purchaseId` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` TEXT NOT NULL, `username` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `role` TEXT NOT NULL, `displayName` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '73dc18b4b48896bc31adbded03f4d362')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `batches`");
        db.execSQL("DROP TABLE IF EXISTS `purchased_raws`");
        db.execSQL("DROP TABLE IF EXISTS `orders`");
        db.execSQL("DROP TABLE IF EXISTS `expenses`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
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
        final HashMap<String, TableInfo.Column> _columnsBatches = new HashMap<String, TableInfo.Column>(13);
        _columnsBatches.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("serverId", new TableInfo.Column("serverId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("number", new TableInfo.Column("number", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("formationDate", new TableInfo.Column("formationDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("purchaseId", new TableInfo.Column("purchaseId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("rawQuantityKg", new TableInfo.Column("rawQuantityKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("outputKg", new TableInfo.Column("outputKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("outputPercent", new TableInfo.Column("outputPercent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("costPrice", new TableInfo.Column("costPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("optimalPricePerKg", new TableInfo.Column("optimalPricePerKg", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("lastModified", new TableInfo.Column("lastModified", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatches.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBatches = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBatches = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBatches = new TableInfo("batches", _columnsBatches, _foreignKeysBatches, _indicesBatches);
        final TableInfo _existingBatches = TableInfo.read(db, "batches");
        if (!_infoBatches.equals(_existingBatches)) {
          return new RoomOpenHelper.ValidationResult(false, "batches(com.wholesale.manager.data.local.entity.BatchEntity).\n"
                  + " Expected:\n" + _infoBatches + "\n"
                  + " Found:\n" + _existingBatches);
        }
        final HashMap<String, TableInfo.Column> _columnsPurchasedRaws = new HashMap<String, TableInfo.Column>(13);
        _columnsPurchasedRaws.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("number", new TableInfo.Column("number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("serverId", new TableInfo.Column("serverId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("lastModified", new TableInfo.Column("lastModified", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("quantityKg", new TableInfo.Column("quantityKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("purchasePriceTotal", new TableInfo.Column("purchasePriceTotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("pricePerKg", new TableInfo.Column("pricePerKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("supplierName", new TableInfo.Column("supplierName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("purchaseDate", new TableInfo.Column("purchaseDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchasedRaws.put("batchId", new TableInfo.Column("batchId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPurchasedRaws = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPurchasedRaws = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPurchasedRaws = new TableInfo("purchased_raws", _columnsPurchasedRaws, _foreignKeysPurchasedRaws, _indicesPurchasedRaws);
        final TableInfo _existingPurchasedRaws = TableInfo.read(db, "purchased_raws");
        if (!_infoPurchasedRaws.equals(_existingPurchasedRaws)) {
          return new RoomOpenHelper.ValidationResult(false, "purchased_raws(com.wholesale.manager.data.local.entity.PurchasedRawEntity).\n"
                  + " Expected:\n" + _infoPurchasedRaws + "\n"
                  + " Found:\n" + _existingPurchasedRaws);
        }
        final HashMap<String, TableInfo.Column> _columnsOrders = new HashMap<String, TableInfo.Column>(15);
        _columnsOrders.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("serverId", new TableInfo.Column("serverId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("lastModified", new TableInfo.Column("lastModified", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("customerName", new TableInfo.Column("customerName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("customerPhone", new TableInfo.Column("customerPhone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("customerAddress", new TableInfo.Column("customerAddress", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("batchId", new TableInfo.Column("batchId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("quantityKg", new TableInfo.Column("quantityKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("pricePerKg", new TableInfo.Column("pricePerKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("totalAmount", new TableInfo.Column("totalAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("creationDate", new TableInfo.Column("creationDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("shipmentDate", new TableInfo.Column("shipmentDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("deliveryMethod", new TableInfo.Column("deliveryMethod", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOrders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOrders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoOrders = new TableInfo("orders", _columnsOrders, _foreignKeysOrders, _indicesOrders);
        final TableInfo _existingOrders = TableInfo.read(db, "orders");
        if (!_infoOrders.equals(_existingOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "orders(com.wholesale.manager.data.local.entity.OrderEntity).\n"
                  + " Expected:\n" + _infoOrders + "\n"
                  + " Found:\n" + _existingOrders);
        }
        final HashMap<String, TableInfo.Column> _columnsExpenses = new HashMap<String, TableInfo.Column>(10);
        _columnsExpenses.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("serverId", new TableInfo.Column("serverId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("lastModified", new TableInfo.Column("lastModified", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("batchIdsJson", new TableInfo.Column("batchIdsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("purchaseId", new TableInfo.Column("purchaseId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExpenses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExpenses = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExpenses = new TableInfo("expenses", _columnsExpenses, _foreignKeysExpenses, _indicesExpenses);
        final TableInfo _existingExpenses = TableInfo.read(db, "expenses");
        if (!_infoExpenses.equals(_existingExpenses)) {
          return new RoomOpenHelper.ValidationResult(false, "expenses(com.wholesale.manager.data.local.entity.ExpenseEntity).\n"
                  + " Expected:\n" + _infoExpenses + "\n"
                  + " Found:\n" + _existingExpenses);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(5);
        _columnsUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.wholesale.manager.data.local.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "73dc18b4b48896bc31adbded03f4d362", "567d09f3a6f3b1939b152efbfdd0e3a2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "batches","purchased_raws","orders","expenses","users");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `batches`");
      _db.execSQL("DELETE FROM `purchased_raws`");
      _db.execSQL("DELETE FROM `orders`");
      _db.execSQL("DELETE FROM `expenses`");
      _db.execSQL("DELETE FROM `users`");
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
    _typeConvertersMap.put(BatchDao.class, BatchDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PurchasedRawDao.class, PurchasedRawDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(OrderDao.class, OrderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExpenseDao.class, ExpenseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
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
  public BatchDao batchDao() {
    if (_batchDao != null) {
      return _batchDao;
    } else {
      synchronized(this) {
        if(_batchDao == null) {
          _batchDao = new BatchDao_Impl(this);
        }
        return _batchDao;
      }
    }
  }

  @Override
  public PurchasedRawDao purchasedRawDao() {
    if (_purchasedRawDao != null) {
      return _purchasedRawDao;
    } else {
      synchronized(this) {
        if(_purchasedRawDao == null) {
          _purchasedRawDao = new PurchasedRawDao_Impl(this);
        }
        return _purchasedRawDao;
      }
    }
  }

  @Override
  public OrderDao orderDao() {
    if (_orderDao != null) {
      return _orderDao;
    } else {
      synchronized(this) {
        if(_orderDao == null) {
          _orderDao = new OrderDao_Impl(this);
        }
        return _orderDao;
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
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }
}
