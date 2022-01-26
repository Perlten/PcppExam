package exercises09;

import java.awt.event.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class ReadWords {


    public static void main(String[] args) {
        new ReadWords(100);
    }

    int n = 0;

    public ReadWords(int n) {
        this.n = n;
        timer2.subscribe(display);

    }

    String filename = "app/src/main/java/resources/english-words.txt";
    BufferedReader reader;

    {
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    final Observable<String> timer = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(@NonNull ObservableEmitter<String> s) throws Exception {
            int count = 0;
            while (reader.readLine() != null && count < n) {
                s.onNext(reader.readLine());
                count++;
            }
        }
    });

    final Observable<String> timer2 = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(@NonNull ObservableEmitter<String> s) throws Exception {
            int count = 0;
            while (reader.readLine() != null && count < n) {

                String word = reader.readLine();
                if(word.length() >= 22) {
                s.onNext(word);
                   count++;
                }

            }
        }
    });

    final Observer<String> display = new Observer<String>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull String s) {
            System.out.println(s);

        }

        @Override
        public void onError(@NonNull Throwable e) {
            System.out.println("errorrrr");
        }

        @Override
        public void onComplete() {
            System.out.println("complete");
        }
    };
}

