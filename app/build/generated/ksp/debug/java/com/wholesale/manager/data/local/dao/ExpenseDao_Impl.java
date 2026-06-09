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
import com.wholesale.manager.data.local.entity.ExpenseEntity;
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
public final class ExpenseDao_Impl implements ExpenseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExpenseEntity> __insertionAdapterOfExpenseEntity;

  private final EntityDeletionOrUpdateAdapter<ExpenseEntity> __updateAdapterOfExpenseEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public ExpenseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExpenseEntity = new EntityInsertionAdapter<ExpenseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `expenses` (`id`,`serverId`,`lastModified`,`isDeleted`,`type`,`amount`,`date`,`description`,`batchIdsJson`,`purchaseId`,`batchId`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExpenseEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getServerId());
        }
        statement.bindString(3, entity.getLastModified());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getType());
        statement.bindDouble(6, entity.getAmount());
        statement.bindString(7, entity.getDate());
        if (entity.getDescription() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDescription());
        }
        statement.bindString(9, entity.getBatchIdsJson());
        if (entity.getPurchaseId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPurchaseId());
        }
        if (entity.getBatchId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getBatchId());
        }
      }
    };
    this.__updateAdapterOfExpenseEntity = new EntityDeletionOrUpdateAdapter<ExpenseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `expenses` SET `id` = ?,`serverId` = ?,`lastModified` = ?,`isDeleted` = ?,`type` = ?,`amount` = ?,`date` = ?,`description` = ?,`batchIdsJson` = ?,`purchaseId` = ?,`batchId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExpenseEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getServerId());
        }
        statement.bindString(3, entity.getLastModified());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getType());
        statement.bindDouble(6, entity.getAmount());
        statement.bindString(7, entity.getDate());
        if (entity.getDescription() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDescription());
        }
        statement.bindString(9, entity.getBatchIdsJson());
        if (entity.getPurchaseId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPurchaseId());
        }
        if (entity.getBatchId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getBatchId());
        }
        statement.bindString(12, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE expenses SET isDeleted = 1, lastModified = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ExpenseEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExpenseEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ExpenseEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExpenseEntity.handle(entity);
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
  public Flow<List<ExpenseEntity>> getAllNotDeleted() {
    final String _sql = "SELECT * FROM expenses WHERE isDeleted = 0 ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"expenses"}, new Callable<List<ExpenseEntity>>() {
      @Override
      @NonNull
      public List<ExpenseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfBatchIdsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "batchIdsJson");
          final int _cursorIndexOfPurchaseId = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseId");
          final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
          final List<ExpenseEntity> _result = new ArrayList<ExpenseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExpenseEntity _item;
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
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpBatchIdsJson;
            _tmpBatchIdsJson = _cursor.getString(_cursorIndexOfBatchIdsJson);
            final String _tmpPurchaseId;
            if (_cursor.isNull(_cursorIndexOfPurchaseId)) {
              _tmpPurchaseId = null;
            } else {
              _tmpPurchaseId = _cursor.getString(_cursorIndexOfPurchaseId);
            }
            final String _tmpBatchId;
            if (_cursor.isNull(_cursorIndexOfBatchId)) {
              _tmpBatchId = null;
            } else {
              _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
            }
            _item = new ExpenseEntity(_tmpId,_tmpServerId,_tmpLastModified,_tmpIsDeleted,_tmpType,_tmpAmount,_tmpDate,_tmpDescription,_tmpBatchIdsJson,_tmpPurchaseId,_tmpBatchId);
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
  public Object getById(final String id, final Continuation<? super ExpenseEntity> $completion) {
    final String _sql = "SELECT * FROM expenses WHERE id = ? AND isDeleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExpenseEntity>() {
      @Override
      @Nullable
      public ExpenseEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfBatchIdsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "batchIdsJson");
          final int _cursorIndexOfPurchaseId = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseId");
          final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
          final ExpenseEntity _result;
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
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpBatchIdsJson;
            _tmpBatchIdsJson = _cursor.getString(_cursorIndexOfBatchIdsJson);
            final String _tmpPurchaseId;
            if (_cursor.isNull(_cursorIndexOfPurchaseId)) {
              _tmpPurchaseId = null;
            } else {
              _tmpPurchaseId = _cursor.getString(_cursorIndexOfPurchaseId);
            }
            final String _tmpBatchId;
            if (_cursor.isNull(_cursorIndexOfBatchId)) {
              _tmpBatchId = null;
            } else {
              _tmpBatchId = _cursor.getString(_cursorIndexOfBatchId);
            }
            _result = new ExpenseEntity(_tmpId,_tmpServerId,_tmpLastModified,_tmpIsDeleted,_tmpType,_tmpAmount,_tmpDate,_tmpDescription,_tmpBatchIdsJson,_tmpPurchaseId,_tmpBatchId);
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
