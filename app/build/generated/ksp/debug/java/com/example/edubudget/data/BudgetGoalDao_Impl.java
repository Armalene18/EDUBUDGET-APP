package com.example.edubudget.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BudgetGoalDao_Impl implements BudgetGoalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BudgetGoal> __insertionAdapterOfBudgetGoal;

  public BudgetGoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudgetGoal = new EntityInsertionAdapter<BudgetGoal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `budget_goal` (`id`,`minAmount`,`maxAmount`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetGoal entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getMinAmount());
        statement.bindDouble(3, entity.getMaxAmount());
      }
    };
  }

  @Override
  public Object insert(final BudgetGoal goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBudgetGoal.insert(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<BudgetGoal>> $completion) {
    final String _sql = "SELECT * FROM budget_goal";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BudgetGoal>>() {
      @Override
      @NonNull
      public List<BudgetGoal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMinAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "minAmount");
          final int _cursorIndexOfMaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "maxAmount");
          final List<BudgetGoal> _result = new ArrayList<BudgetGoal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BudgetGoal _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpMinAmount;
            _tmpMinAmount = _cursor.getDouble(_cursorIndexOfMinAmount);
            final double _tmpMaxAmount;
            _tmpMaxAmount = _cursor.getDouble(_cursorIndexOfMaxAmount);
            _item = new BudgetGoal(_tmpId,_tmpMinAmount,_tmpMaxAmount);
            _result.add(_item);
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
