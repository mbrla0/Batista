#include <cstdint>
#include <iostream>
#include <vector>
#include <queue>

int main()
{
	size_t node_count, rules;
	for(;;)
	{
		std::cin >> node_count;
		std::cin >> rules;

		if(node_count == 0 && rules == 0)
			break;

		auto nodes = std::vector<bool>(node_count * node_count);
		auto node_at = [&nodes, node_count](size_t x, size_t y) {
			if(x >= nodes.size())
				throw std::runtime_error(
					"Requested value whose X position is larger than the container");
			if(y >= nodes.size())
				throw std::runtime_error(
					"Requested value whose Y position is larger than the container");

			return nodes.at(y * node_count + x);
		};
		auto incoming = std::vector<uint8_t>(node_count);

		for(size_t i = 0; i < rules; ++i)
		{
			size_t source, target;
			std::cin >> source;
			std::cin >> target;

			node_at(source - 1, target - 1) = true;
			incoming.at(target - 1) += 1;
		}

		auto queue = std::queue<size_t>();
		for(size_t i = 0; i < node_count; ++i)
			if(incoming.at(i) == 0)
				queue.push(i);

		auto solution = std::vector<size_t>();
		solution.reserve(node_count);

		while(!queue.empty())
		{
			size_t current = queue.front();

			solution.push_back(current);
			for(size_t i = 0; i < node_count; ++i)
				if(node_at(current, i))
				{
					incoming.at(i) -= 1;
					if(incoming.at(i) == 0)
						queue.push(i);
				}

			queue.pop();
		}

		for(auto &i: solution)
			std::cout << i + 1 << " ";
		std::cout << std::endl;
	}

	return 0;
}
