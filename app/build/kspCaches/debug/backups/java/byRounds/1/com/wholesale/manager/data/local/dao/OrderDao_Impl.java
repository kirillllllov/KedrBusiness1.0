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
import com.wholesale.manager.data.local.entity.OrderEntity;
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
public final class OrderDao_Impl implements OrderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<OrderEntity> __insertionAdapterOfOrderEntity;

  private final EntityDeletionOrUpdateAdapter<OrderEntity> __updateAdapterOfOrderEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public OrderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOrderEntity = new EntityInsertionAdapter<OrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `orders` (`id`,`serverId`,`lastModified`,`isDeleted`,`customerName`,`customerPhone`,`customerAddress`,`batchId`,`quantityKg`,`pricePerKg`,`totalAmount`,`creationDate`,`shipmentDate`,`deliveryMethod`,`status`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getServerId());
        }
        statement.bindString(3, entity.getLastModified());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getCustomerName());
        statement.bindString(6, entity.getCustomerPhone());
        if (entity.getCustomerAddress() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCustomerAddress());
        }
        statement.bindString(8, entity.getBatchId());
        statement.bindDouble(9, entity.getQuantityKg());
        statement.bindDouble(10, entity.getPricePerKg());
        statement.bindDouble(11, entity.getTotalAmount());
        statement.bindString(12, entity.getCreationDate());
        if (entity.getShipmentDate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getShipmentDate());
        }
        statement.bindString(14, entity.getDeliveryMethod());
        statement.bindString(15, entity.getStatus());
      }
    };
    this.__updateAdapterOfOrderEntity = new EntityDeletionOrUpdateAdapter<OrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `orders` SET `id` = ?,`serverId` = ?,`lastModified` = ?,`isDeleted` = ?,`customerName` = ?,`customerPhone` = ?,`customerAddress` = ?,`batchId` = ?,`quantityKg` = ?,`pricePerKg` = ?,`totalAmount` = ?,`creationDate` = ?,`shipmentDate` = ?,`deliveryMethod` = ?,`status` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getServerId());
        }
        statement.bindString(3, entity.getLastModified());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getCustomerName());
        statement.bindString(6, entity.getCustomerPhone());
        if (entity.getCustomerAddress() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCustomerAddress());
        }
        statement.bindString(8, entity.getBatchId());
        statement.bindDouble(9, entity.getQuantityKg());
        statement.bindDouble(10, entity.getPricePerKg());
        statement.bindDouble(11, entity.getTotalAmount());
        statement.bindString(12, entity.getCreationDate());
        if (entity.getShipmentDate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getShipmentDate());
        }
        statement.bindString(14, entity.getDeliveryMethod());
        statement.bindString(15, entity.getStatus());
        statement.bindString(16, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE orders SET isDeleted = 1, lastModified = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final OrderEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfOrderEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final OrderEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfOrderEntity.handle(entity);
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
  public Flow<List<OrderEntity>> getAllNotDeleted() {
    final String _sql = "SELECT * FROM orders WHERE isDeleted = 0 ORDER BY creationDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"orders"}, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customerPhone");
          final int _cursorIndexOfCustomerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "customerAddress");
          final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
          final int _cursorIndexOfQuantityKg = CursorUtil.getColumnIndexOrThrow(_cursor, "quantityKg");
          final int _cursorIndexOfPricePerKg = CursorUtil.getColumnIndexOrThrow(_cursor, "pricePerKg");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final int _cursorIndexOfCreationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "creationDate");
          final int _cursorIndexOfShipmentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "shipmentDate");
          final int _cursorIndexOfDeliveryMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryMethod");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
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
            final String _tmpCustomerName;
            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            final String _tmpCustomerPhone;
            _tmpCustomerPhone = _cursor.getString(_cursorIndexOfCustomerPhone);
            final String _tmpCustomerAddress;
            if (_cursor.isNull(_cursorIndexOfCustomerAddress)) {
              _tmpCustomerAddress = null;
            } else {
              _tmpCustomerAddress = _cursor.getString(_cursorIndexOfCustomerAddress);
            }
            final String _tmpBatchId;
            _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
            final double _tmpQuantityKg;
            _tmpQuantityKg = _cursor.getDouble(_cursorIndexOfQuantityKg);
            final double _tmpPricePerKg;
            _tmpPricePerKg = _cursor.getDouble(_cursorIndexOfPricePerKg);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            final String _tmpCreationDate;
            _tmpCreationDate = _cursor.getString(_cursorIndexOfCreationDate);
            final String _tmpShipmentDate;
            if (_cursor.isNull(_cursorIndexOfShipmentDate)) {
              _tmpShipmentDate = null;
            } else {
              _tmpShipmentDate = _cursor.getString(_cursorIndexOfShipmentDate);
            }
            final String _tmpDeliveryMethod;
            _tmpDeliveryMethod = _cursor.getString(_cursorIndexOfDeliveryMethod);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new OrderEntity(_tmpId,_tmpServerId,_tmpLastModified,_tmpIsDeleted,_tmpCustomerName,_tmpCustomerPhone,_tmpCustomerAddress,_tmpBatchId,_tmpQuantityKg,_tmpPricePerKg,_tmpTotalAmount,_tmpCreationDate,_tmpShipmentDate,_tmpDeliveryMethod,_tmpStatus);
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
  public Object getById(final String id, final Continuation<? super OrderEntity> $completion) {
    final String _sql = "SELECT * FROM orders WHERE id = ? AND isDeleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<OrderEntity>() {
      @Override
      @Nullable
      public OrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customerPhone");
          final int _cursorIndexOfCustomerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "customerAddress");
          final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
          final int _cursorIndexOfQuantityKg = CursorUtil.getColumnIndexOrThrow(_cursor, "quantityKg");
          final int _cursorIndexOfPricePerKg = CursorUtil.getColumnIndexOrThrow(_cursor, "pricePerKg");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final int _cursorIndexOfCreationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "creationDate");
          final int _cursorIndexOfShipmentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "shipmentDate");
          final int _cursorIndexOfDeliveryMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryMethod");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final OrderEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
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
            final String _tmpCustomerName;
            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            final String _tmpCustomerPhone;
            _tmpCustomerPhone = _cursor.getString(_cursorIndexOfCustomerPhone);
            final String _tmpCustomerAddress;
            if (_cursor.isNull(_cursorIndexOfCustomerAddress)) {
              _tmpCustomerAddress = null;
            } else {
              _tmpCustomerAddress = _cursor.getString(_cursorIndexOfCustomerAddress);
            }
            final String _tmpBatchId;
            _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
            final double _tmpQuantityKg;
            _tmpQuantityKg = _cursor.getDouble(_cursorIndexOfQuantityKg);
            final double _tmpPricePerKg;
            _tmpPricePerKg = _cursor.getDouble(_cursorIndexOfPricePerKg);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            final String _tmpCreationDate;
            _tmpCreationDate = _cursor.getString(_cursorIndexOfCreationDate);
            final String _tmpShipmentDate;
            if (_cursor.isNull(_cursorIndexOfShipmentDate)) {
              _tmpShipmentDate = null;
            } else {
              _tmpShipmentDate = _cursor.getString(_cursorIndexOfShipmentDate);
            }
            final String _tmpDeliveryMethod;
            _tmpDeliveryMethod = _cursor.getString(_cursorIndexOfDeliveryMethod);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _result = new OrderEntity(_tmpId,_tmpServerId,_tmpLastModified,_tmpIsDeleted,_tmpCustomerName,_tmpCustomerPhone,_tmpCustomerAddress,_tmpBatchId,_tmpQuantityKg,_tmpPricePerKg,_tmpTotalAmount,_tmpCreationDate,_tmpShipmentDate,_tmpDeliveryMethod,_tmpStatus);
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
