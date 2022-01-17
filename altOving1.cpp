#include <algorithm>
#include <execution>
#include <iostream>
#include <vector>
#include <bits/stdc++.h>

using namespace std;

class CheckPrime {

public:
    static int checkPrime(int nr) {
        if (nr < 2) return -1;
        for (int i = 2; i < nr; ++i) {
            if (nr % i == 0) return -1;
        }
        return nr;
    }
};

int main () {
    int start = 1000;
    int stop = 10000;
    int primes[stop - start];
    vector<int> numbers;
    for (int i = start; i < stop; ++i) {
        numbers.push_back(i);
    }
    //for (auto &val: numbers) cout << val << " ";
    transform(execution::par, numbers.begin(), numbers.end(), primes, CheckPrime::checkPrime);

    vector<int> res;


    for (auto &value: primes) {
        if(value != -1) res.push_back(value);
    };

    for (auto &val: res) {
        cout << val << " ";
    }

}