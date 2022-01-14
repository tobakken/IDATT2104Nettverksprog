#include <iostream>
#include <thread>
#include <vector>
#include <mutex>

using namespace std;

class CheckPrime {
    public:
        static bool checkPrime(int nr) {
            if (nr < 2) return false;
            for (int i = 2; i < nr; ++i) {
                if (nr % i == 0) return false;
            }
            return true;
        }
    };

int main() {
    vector<thread> threads;
    vector<int> primes;
    int counter = 0;
    mutex counter_mutex;
    int stop = 100;
    int nrOfThreads = 4;

    threads.reserve(nrOfThreads);
for (int i = 0; i < nrOfThreads; ++i) {
        threads.emplace_back([&counter, &counter_mutex, &stop, &primes] {
            while (counter < stop) {
                counter_mutex.lock();
                if (CheckPrime::checkPrime(counter)) {
                    primes.push_back(counter);
                }
                ++counter;
                counter_mutex.unlock();
            }
        });
    }

    for (auto const &value: primes) {cout << value << endl;}
    for (auto &thread: threads) {thread.join();}
    cout << "Hello, World!" << endl;
    return 0;
}

