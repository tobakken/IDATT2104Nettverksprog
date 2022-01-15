#include <iostream>
#include <thread>
#include <vector>
#include <bits/stdc++.h>
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
    int start = 0;
    int stop = 100;
    int nrOfThreads = 5;
    int nrPrThread = (stop - start) / nrOfThreads;

    threads.reserve(nrOfThreads);
for (int i = 0; i < nrOfThreads; ++i) {
    int nrToCheck = start + (nrPrThread * i);
    stop = (nrPrThread + start)*(i+1);
        threads.emplace_back([nrToCheck, stop, &primes] {
            int check = nrToCheck;
            int stopCheck = stop;
            while (check < (stopCheck)) {
                if (CheckPrime::checkPrime(check)) {
                    primes.push_back(check);
                }
                ++check;
            }
        });
    }

    for (auto &thread: threads) {thread.join();}

    cout << "Execution time: " << duration_cast<microseconds>(high_resolution_clock::now() - startTime).count() << endl;
    cout << "Hello, World!" << endl;
    sort(primes.begin(), primes.end());
    for (auto const &value: primes) {cout << value << " ";}
    return 0;
}

