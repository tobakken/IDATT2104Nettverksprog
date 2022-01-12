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
    int start = 0;
    int stop = 10;
    int nrOfThreads = 4;

    threads.reserve(nrOfThreads);
for (int i = 0; i < nrOfThreads; ++i) {
        threads.emplace_back([&start, &stop, &primes] {
            int j = start;
            while (j < stop) {
                if (CheckPrime::checkPrime(j)) primes.push_back(j); //Problemer med at flere leser samme tall.
                ++j;                                                   //Må finne ut hvor jeg skal bruke lås.
            }
        });
    }

    for (auto const &value: primes) cout << value << endl;
    for (auto &thread: threads) thread.join();
    cout << "Hello, World!" << endl;
    return 0;
}

