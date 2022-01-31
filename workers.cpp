#include <iostream>
#include <thread>
#include <functional>
#include <vector>
#include <list>
#include <condition_variable>
#include <mutex>

using namespace std;

int main() {
    cout << "Hello" << endl;
}

class Workers {
public:
    int nrOfThreads;
    vector<thread> worker_threads;
    list<function<void()>> tasks;
    mutex task_mutex;
    condition_variable cv;

    Workers(int nr) {
        nrOfThreads = nr;
    }

    void start() {
        for (int i = 0; i < nrOfThreads; ++i) {
            worker_threads.emplace_back([this] {
                while (true){
                    function<void()> task;
                    if (!tasks.empty()) {

                        task = *tasks.begin();
                        tasks.pop_front();
                    }
                    task();
                }
            });
        }
    }

    void post(function<void()> func) {
        tasks.emplace_back(func);
    }

    void join() {
        for (auto &thread: worker_threads) thread.join();
    }
};