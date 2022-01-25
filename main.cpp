#include <iostream>
#include <thread>
#include <vector>
#include <bits/stdc++.h>
#include <chrono>
#include <mutex>

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
    mutex prime_mutex;
    int start = 0;
    int stop = 101;
    int nrOfThreads = 10;
    int nrPrThread = (stop - start) / nrOfThreads;

    threads.reserve(nrOfThreads);
    for (int i = 0; i < nrOfThreads; ++i) {
        int nrToCheck = start + (nrPrThread * i);
        int stopVal = (i == nrOfThreads - 1) ? stop + 1 : min(stop, (nrPrThread*(i+1) + start));

        threads.emplace_back([nrToCheck, stopVal, &primes, &prime_mutex] {
            int check = nrToCheck;
            while (check < (stopVal)) {
                if (CheckPrime::checkPrime(check)) {
                    unique_lock<mutex> lock(prime_mutex);
                    primes.push_back(check);
                }
                ++check;
            }
        });
    }

    for (auto &thread: threads) { thread.join(); }

    cout << "Execution time: " << duration_cast<microseconds>(high_resolution_clock::now() - startTime).count() << endl;
    cout << "Hello, World!" << endl;
    sort(primes.begin(), primes.end());
    for (auto const &value: primes) { cout << value << " "; }
    return 0;
}

