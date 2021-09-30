#include <cstdint>
#include <vector>
#include <iostream>
#include <algorithm>
#include <cstdio>

template<typename T>
static inline T distance(T a, T b)
{
	if(a > b)
		return a - b;
	else
		return b - a;
}

void run()
{
	size_t house_count;
	size_t router_count;

	std::cin >> router_count;
	std::cin >> house_count;

	auto dataset = std::vector<uint_fast32_t>(house_count);
	for(auto& house_number : dataset)
	{
		std::cin >> house_number;
	}
	std::sort(dataset.begin(),  dataset.end());

	double left = 0;
	double right = dataset.back() - dataset.front();

	for(size_t i = 0; i < 50; ++i)
	{
		double mid = (left + right) / 2;

		size_t used_routers = 0;
		double dist = -1e300;

		for(const auto& house_number : dataset)
		{
			if(dist < house_number)
			{
				dist = house_number + 2 * mid;
				used_routers += 1;
			}
		}

		if(used_routers <= router_count)
			right = mid;
		else
			left = mid;
	}

	fprintf(stdout, "%.1lf\n", right);
}

int main()
{
	size_t runs;
	std::cin >> runs;

	for(size_t i = 0; i < runs; ++i)
		run();
}
