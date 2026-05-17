package com.wholesale.manager.data.local.dao;

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
import com.wholesale.manager.data.local.entity.PurchasedRawEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
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
public final class PurchasedRawDao_Impl implements PurchasedRawDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PurchasedRawEntity> __insertionAdapterOfPurchasedRawEntity;

  private final EntityDeletionOrUpdateAdapter<PurchasedRawEntity> __updateAdapterOfPurchasedRawEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public PurchasedRawDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPurchasedRawEntity = new EntityInsertionAdapter<PurchasedRawEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `purchased_raws` (`id`,`number`,`serverId`,`lastModified`,`isDeleted`,`type`,`quantityKg`,`purchasePriceTotal`,`pricePerKg`,`supplierName`,`purchaseDate`,`status`,`batchId`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PurchasedRawEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindLong(2, entity.getNumber());
        if (entity.getServerId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getServerId());
        }
        statement.bindString(4, entity.getLastModified());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindString(6, entity.getType());
        statement.bindDouble(7, entity.getQuantityKg());
        statement.bindDouble(8, entity.getPurchasePriceTotal());
        statement.bindDouble(9, entity.getPricePerKg());
        statement.bindString(10, entity.getSupplierName());
        statement.bindString(11, entity.getPurchaseDate());
        statement.bindString(12, entity.getStatus());
        if (entity.getBatchId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getBatchId());
        }
      }
    };
    this.__updateAdapterOfPurchasedRawEntity = new EntityDeletionOrUpdateAdapter<PurchasedRawEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `purchased_raws` SET `id` = ?,`number` = ?,`serverId` = ?,`lastModified` = ?,`isDeleted` = ?,`type` = ?,`quantityKg` = ?,`purchasePriceTotal` = ?,`pricePerKg` = ?,`supplierName` = ?,`purchaseDate` = ?,`status` = ?,`batchId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PurchasedRawEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindLong(2, entity.getNumber());
        if (entity.getServerId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getServerId());
        }
        statement.bindString(4, entity.getLastModified());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindString(6, entity.getType());
        statement.bindDouble(7, entity.getQuantityKg());
        statement.bindDouble(8, entity.getPurchasePriceTotal());
        statement.bindDouble(9, entity.getPricePerKg());
        statement.bindString(10, entity.getSupplierName());
        statement.bindString(11, entity.getPurchaseDate());
        statement.bindString(12, entity.getStatus());
        if (entity.getBatchId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getBatchId());
        }
        statement.bindString(14, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE purchased_raws SET isDeleted = 1, lastModified = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PurchasedRawEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPurchasedRawEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PurchasedRawEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPurchasedRawEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object softDelete(final String id, final String timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDelete.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfSoftDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PurchasedRawEntity>> getAllNotDeleted() {
    final String _sql = "SELECT * FROM purchased_raws WHERE isDeleted = 0 ORDER BY purchaseDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"purchased_raws"}, new Callable<List<PurchasedRawEntity>>() {
      @Override
      @NonNull
      public List<PurchasedRawEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfQuantityKg = CursorUtil.getColumnIndexOrThrow(_cursor, "quantityKg");
          final int _cursorIndexOfPurchasePriceTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "purchasePriceTotal");
          final int _cursorIndexOfPricePerKg = CursorUtil.getColumnIndexOrThrow(_cursor, "pricePerKg");
          final int _cursorIndexOfSupplierName = CursorUtil.getColumnIndexOrThrow(_cursor, "supplierName");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
          final List<PurchasedRawEntity> _result = new ArrayList<PurchasedRawEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PurchasedRawEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final int _tmpNumber;
            _tmpNumber = _cursor.getInt(_cursorIndexOfNumber);
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpLastModified;
            _tmpLastModified = _cursor.getString(_cursorIndexOfLastModified);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpQuantityKg;
            _tmpQuantityKg = _cursor.getDouble(_cursorIndexOfQuantityKg);
            final double _tmpPurchasePriceTotal;
            _tmpPurchasePriceTotal = _cursor.getDouble(_cursorIndexOfPurchasePriceTotal);
            final double _tmpPricePerKg;
            _tmpPricePerKg = _cursor.getDouble(_cursorIndexOfPricePerKg);
            final String _tmpSupplierName;
            _tmpSupplierName = _cursor.getString(_cursorIndexOfSupplierName);
            final String _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getString(_cursorIndexOfPurchaseDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpBatchId;
            if (_cursor.isNull(_cursorIndexOfBatchId)) {
              _tmpBatchId = null;
            } else {
              _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
            }
            _item = new PurchasedRawEntity(_tmpId,_tmpNumber,_tmpServerId,_tmpLastModified,_tmpIsDeleted,_tmpType,_tmpQuantityKg,_tmpPurchasePriceTotal,_tmpPricePerKg,_tmpSupplierName,_tmpPurchaseDate,_tmpStatus,_tmpBatchId);
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

  @Override
  public Object getById(final String id,
      final Continuation<? super PurchasedRawEntity> $completion) {
    final String _sql = "SELECT * FROM purchased_raws WHERE id = ? AND isDeleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PurchasedRawEntity>() {
      @Override
      @Nullable
      public PurchasedRawEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfQuantityKg = CursorUtil.getColumnIndexOrThrow(_cursor, "quantityKg");
          final int _cursorIndexOfPurchasePriceTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "purchasePriceTotal");
          final int _cursorIndexOfPricePerKg = CursorUtil.getColumnIndexOrThrow(_cursor, "pricePerKg");
          final int _cursorIndexOfSupplierName = CursorUtil.getColumnIndexOrThrow(_cursor, "supplierName");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
          final PurchasedRawEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final int _tmpNumber;
            _tmpNumber = _cursor.getInt(_cursorIndexOfNumber);
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpLastModified;
            _tmpLastModified = _cursor.getString(_cursorIndexOfLastModified);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpQuantityKg;
            _tmpQuantityKg = _cursor.getDouble(_cursorIndexOfQuantityKg);
            final double _tmpPurchasePriceTotal;
            _tmpPurchasePriceTotal = _cursor.getDouble(_cursorIndexOfPurchasePriceTotal);
            final double _tmpPricePerKg;
            _tmpPricePerKg = _cursor.getDouble(_cursorIndexOfPricePerKg);
            final String _tmpSupplierName;
            _tmpSupplierName = _cursor.getString(_cursorIndexOfSupplierName);
            final String _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getString(_cursorIndexOfPurchaseDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpBatchId;
            if (_cursor.isNull(_cursorIndexOfBatchId)) {
              _tmpBatchId = null;
            } else {
              _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
            }
            _result = new PurchasedRawEntity(_tmpId,_tmpNumber,_tmpServerId,_tmpLastModified,_tmpIsDeleted,_tmpType,_tmpQuantityKg,_tmpPurchasePriceTotal,_tmpPricePerKg,_tmpSupplierName,_tmpPurchaseDate,_tmpStatus,_tmpBatchId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
