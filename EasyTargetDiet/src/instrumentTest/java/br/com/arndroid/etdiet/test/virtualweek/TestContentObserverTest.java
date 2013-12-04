package br.com.arndroid.etdiet.test.virtualweek;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.virtualweek.TestContentObserver;
import sk.m217.tests.utils.ProviderTestCase3;

public class TestContentObserverTest extends ProviderTestCase3<Provider> {

    public TestContentObserverTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();
    }

    public void testMultiThreadV2() throws InterruptedException {
        final Context context = getMockContext();
        final TestContentObserver mObserver = new TestContentObserver();

        final String dateId = DateUtil.dateToDateId(new Date());


        registerContentObserver(Uri.withAppendedPath(
                Contract.FoodsUsage.DATE_ID_CONTENT_URI, dateId), true, mObserver);

        final FoodsUsageManager foodsManager = new FoodsUsageManager(context);
        FoodsUsageEntity foodsEntity = new FoodsUsageEntity(null, dateId,
                Meals.BREAKFAST, 1, "food for test", 7.7f);
        foodsManager.refresh(foodsEntity);

        awaitDelayedSignal(500, 0);
        assertTrue(mObserver.mCalled);
        getMockContext().getContentResolver().unregisterContentObserver(mObserver);
    }

    private void awaitDelayedSignal(final int delayMillis, final int timeOutMillis) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                latch.countDown();
            }
        }, delayMillis);

        if (timeOutMillis > 0) {
            latch.await(timeOutMillis, TimeUnit.MILLISECONDS);
        } else {
            latch.await();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + TestContentObserverTest.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
