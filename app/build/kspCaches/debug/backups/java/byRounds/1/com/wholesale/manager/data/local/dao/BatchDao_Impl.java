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
import com.wholesale.manager.data.local.entity.BatchEntity;
import java.lang.Class;
import java.lang.Double;
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
public final class BatchDao_Impl implements BatchDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BatchEntity> __insertionAdapterOfBatchEntity;

  private final EntityDeletionOrUpdateAdapter<BatchEntity> __updateAdapterOfBatchEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public BatchDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBatchEntity = new EntityInsertionAdapter<BatchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `batches` (`id`,`serverId`,`number`,`formationDate`,`rawQuantityKg`,`outputPercent`,`costPrice`,`optimalPricePerKg`,`status`,`lastModified`,`isDeleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BatchEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getServerId());
        }
        statement.bindString(3, entity.getNumber());
        statement.bindString(4, entity.getFormationDate());
        statement.bindDouble(5, entity.getRawQuantityKg());
        statement.bindLong(6, entity.getOutputPercent());
        statement.bindDouble(7, entity.getCostPrice());
        if (entity.getOptimalPricePerKg() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getOptimalPricePerKg());
        }
        statement.bindString(9, entity.getStatus());
        statement.bindString(10, entity.getLastModified());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(11, _tmp);
      }
    };
    this.__updateAdapterOfBatchEntity = new EntityDeletionOrUpdateAdapter<BatchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `batches` SET `id` = ?,`serverId` = ?,`number` = ?,`formationDate` = ?,`rawQuantityKg` = ?,`outputPercent` = ?,`costPrice` = ?,`optimalPricePerKg` = ?,`status` = ?,`lastModified` = ?,`isDeleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BatchEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getServerId());
        }
        statement.bindString(3, entity.getNumber());
        statement.bindString(4, entity.getFormationDate());
        statement.bindDouble(5, entity.getRawQuantityKg());
        statement.bindLong(6, entity.getOutputPercent());
        statement.bindDouble(7, entity.getCostPrice());
        if (entity.getOptimalPricePerKg() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getOptimalPricePerKg());
        }
        statement.bindString(9, entity.getStatus());
        statement.bindString(10, entity.getLastModified());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(11, _tmp);
        statement.bindString(12, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE batches SET isDeleted = 1, lastModified = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final BatchEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBatchEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BatchEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBatchEntity.handle(entity);
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
  public Flow<List<BatchEntity>> getAllNotDeleted() {
    final String _sql = "SELECT * FROM batches WHERE isDeleted = 0 ORDER BY formationDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"batches"}, new Callable<List<BatchEntity>>() {
      @Override
      @NonNull
      public List<BatchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfFormationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "formationDate");
          final int _cursorIndexOfRawQuantityKg = CursorUtil.getColumnIndexOrThrow(_cursor, "rawQuantityKg");
          final int _cursorIndexOfOutputPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "outputPercent");
          final int _cursorIndexOfCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "costPrice");
          final int _cursorIndexOfOptimalPricePerKg = CursorUtil.getColumnIndexOrThrow(_cursor, "optimalPricePerKg");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final List<BatchEntity> _result = new ArrayList<BatchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BatchEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpNumber;
            _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            final String _tmpFormationDate;
            _tmpFormationDate = _cursor.getString(_cursorIndexOfFormationDate);
            final double _tmpRawQuantityKg;
            _tmpRawQuantityKg = _cursor.getDouble(_cursorIndexOfRawQuantityKg);
            final int _tmpOutputPercent;
            _tmpOutputPercent = _cursor.getInt(_cursorIndexOfOutputPercent);
            final double _tmpCostPrice;
            _tmpCostPrice = _cursor.getDouble(_cursorIndexOfCostPrice);
            final Double _tmpOptimalPricePerKg;
            if (_cursor.isNull(_cursorIndexOfOptimalPricePerKg)) {
              _tmpOptimalPricePerKg = null;
            } else {
              _tmpOptimalPricePerKg = _cursor.getDouble(_cursorIndexOfOptimalPricePerKg);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpLastModified;
            _tmpLastModified = _cursor.getString(_cursorIndexOfLastModified);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            _item = new BatchEntity(_tmpId,_tmpServerId,_tmpNumber,_tmpFormationDate,_tmpRawQuantityKg,_tmpOutputPercent,_tmpCostPrice,_tmpOptimalPricePerKg,_tmpStatus,_tmpLastModified,_tmpIsDeleted);
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
  public Object getById(final String id, final Continuation<? super BatchEntity> $completion) {
    final String _sql = "SELECT * FROM batches WHERE id = ? AND isDeleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BatchEntity>() {
      @Override
      @Nullable
      public BatchEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfFormationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "formationDate");
          final int _cursorIndexOfRawQuantityKg = CursorUtil.getColumnIndexOrThrow(_cursor, "rawQuantityKg");
          final int _cursorIndexOfOutputPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "outputPercent");
          final int _cursorIndexOfCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "costPrice");
          final int _cursorIndexOfOptimalPricePerKg = CursorUtil.getColumnIndexOrThrow(_cursor, "optimalPricePerKg");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final BatchEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpNumber;
            _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            final String _tmpFormationDate;
            _tmpFormationDate = _cursor.getString(_cursorIndexOfFormationDate);
            final double _tmpRawQuantityKg;
            _tmpRawQuantityKg = _cursor.getDouble(_cursorIndexOfRawQuantityKg);
            final int _tmpOutputPercent;
            _tmpOutputPercent = _cursor.getInt(_cursorIndexOfOutputPercent);
            final double _tmpCostPrice;
            _tmpCostPrice = _cursor.getDouble(_cursorIndexOfCostPrice);
            final Double _tmpOptimalPricePerKg;
            if (_cursor.isNull(_cursorIndexOfOptimalPricePerKg)) {
              _tmpOptimalPricePerKg = null;
            } else {
              _tmpOptimalPricePerKg = _cursor.getDouble(_cursorIndexOfOptimalPricePerKg);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpLastModified;
            _tmpLastModified = _cursor.getString(_cursorIndexOfLastModified);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            _result = new BatchEntity(_tmpId,_tmpServerId,_tmpNumber,_tmpFormationDate,_tmpRawQuantityKg,_tmpOutputPercent,_tmpCostPrice,_tmpOptimalPricePerKg,_tmpStatus,_tmpLastModified,_tmpIsDeleted);
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
