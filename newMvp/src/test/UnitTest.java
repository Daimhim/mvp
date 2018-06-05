package test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import toolbox.QLog;

/**
 * Created by Daimhim on 2017/8/7.
 */
public class UnitTest {

    protected static String TAG = "UnitTest";

    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        QLog.v(TAG,thread.getName());
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
                QLog.v(TAG,"QLog:1");

                e.onNext("2");
                QLog.v(TAG,"QLog:2");

                e.onNext("3");
                QLog.v(TAG,"QLog:3");
            }
        })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Thread thread = Thread.currentThread();
                        QLog.v(TAG,"apply:"+thread.getName());
                        return s;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ptint(s);
                    }
                });
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private synchronized static void ptint(String s){
        Thread thread = Thread.currentThread();
        QLog.v(TAG,"accept:"+thread.getName());
        QLog.v(TAG,s);
    }
}
