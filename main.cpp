#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <chrono>

using namespace std;
using namespace chrono;

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
    auto startTime = high_resolution_clock::now();
    vector<thread> threads;
    vector<int> primes;
    int counter = 0;
    mutex counter_mutex;
    int stop = 10000;
    int nrOfThreads = 5;

    threads.reserve(nrOfThreads);
for (int i = 0; i < nrOfThreads; ++i) {
        threads.emplace_back([i, &counter, &counter_mutex, &stop, &primes] {
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

    for (auto &thread: threads) {thread.join();}

    cout << "Execution time: " << duration_cast<microseconds>(high_resolution_clock::now() - startTime).count() << endl;
    cout << "Hello, World!" << endl;
    for (auto const &value: primes) {cout << value << " ";}
    return 0;
}

