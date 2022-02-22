#include <iostream>
#include <thread>
#include <functional>
#include <vector>
#include <list>
#include <condition_variable>
#include <mutex>
#include <chrono>

using namespace std;
using namespace chrono;


class Workers {
private:
    int nrOfThreads;
    vector<thread> worker_threads;
    list<function<void()>> tasks;
    mutex task_mutex;
    condition_variable cv;
    bool thread_run;
    bool unfinished_tasks;
public:
    Workers(int nr) {
        nrOfThreads = nr;
    }

    void start() {
        thread_run = true;
        for (int i = 0; i < nrOfThreads; ++i) {
            worker_threads.emplace_back([this] {
                while (thread_run){
                    function<void()> task;
                    {
                        unique_lock<mutex> lock(task_mutex);
                        while(!unfinished_tasks){
                            cv.wait(lock);
                        }
                        if (!tasks.empty()) {
                            task = *tasks.begin();
                            tasks.pop_front();
                        }
                    }
                    if (task) task();
                }
            });
        }
    }

    void post(function<void()> func) {
        unique_lock<mutex> lock(task_mutex);
        tasks.emplace_back(func);
        unfinished_tasks = true;
        cv.notify_one();
    }

    void post_timeout(function<void()> func, int s){
        this_thread::sleep_for(milliseconds (s));
        unique_lock<mutex> lock(task_mutex);
        tasks.emplace_back(func);
        unfinished_tasks = true;
        cv.notify_one();
    }

    void stop() {
        thread_run = false;
        cv.notify_all();
        for (auto &thread: worker_threads) thread.join();
    }
};

int main() {
    Workers worker_threads(5);
    Workers event_loop(1);

    worker_threads.start();
    event_loop.start();

    for (int i = 0; i < 7; ++i) {
        worker_threads.post([i] {
            for (int j = 0; j < 1000000; ++j) {
                for (int k = 0; k < 1000; ) {
                    k++;
                }
            }
            cout << "worker_threads task: " << i << ", running in thread: " << this_thread::get_id() << endl;
        });
    }

    for (int i = 0; i < 5; ++i) {
        worker_threads.post([i] {
            for (int j = 0; j < 1000000; ++j) {
                for (int k = 0; k < 1000; ) {
                    k++;
                }
            }
            cout << "event_loop task: " << i << ", running in thread: " << this_thread::get_id() << endl;
        });
    }

    worker_threads.post_timeout([] {
        cout << "timed task, running in thread:" << this_thread::get_id() << endl;
    }, 5000);

    worker_threads.post([] {
        cout << "different task, running in thread:" << this_thread::get_id() << endl;
    });

    this_thread::sleep_for(3s);
    cout<<"wait finished"<<endl;
    worker_threads.stop();
    event_loop.stop();
}