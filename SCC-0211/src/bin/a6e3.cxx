#include <iostream>
#include <cstdint>
#include <queue>
#include <vector>
#include <list>
#include <unordered_map>
#include <unordered_set>
#include <set>

struct Edge
{
	size_t a;
	size_t b;
	uint16_t weight;

	std::pair<size_t, size_t> pair() const
	{
		return std::make_pair(a, b);
	}

	std::pair<size_t, size_t> rev_pair() const
	{
		return std::make_pair(b, a);
	}

	template<typename T>
	bool found_in(const T& container) const
	{
		const auto d = container.find(pair()) != container.end();
		const auto r = container.find(rev_pair()) != container.end();

		return d || r;
	}
};

struct ByWeight {
	bool operator ()(const Edge& a, const Edge &b)
	{
		return a.weight > b.weight;
	}
};

using EdgeQueue = std::priority_queue<Edge, std::vector<Edge>, ByWeight>;
using AdjacencyList = std::unordered_map<size_t, std::list<Edge>>;
using Blacklist = std::set<std::pair<size_t, size_t>>;

template<typename T>
AdjacencyList mst(const AdjacencyList& source, const T& blacklist)
{
	auto queue  = EdgeQueue{};
	auto target = AdjacencyList{};

	target[source.begin()->first] = std::list<Edge>{};
	for(const auto& edge : source.begin()->second)
		if(!edge.found_in(blacklist))
			queue.push(edge);

	while(!queue.empty())
	{
		const auto min = queue.top();
		queue.pop();

		const auto as = target.find(min.a) != target.end();
		const auto bs = target.find(min.b) != target.end();

		if(as && bs)
		{
			/* These nodes are already connected. */
			continue;
		}

		auto ea = Edge{};
		ea.a = min.a;
		ea.b = min.b;
		ea.weight = min.weight;

		auto eb = Edge{};
		eb.a = min.b;
		eb.b = min.a;
		eb.weight = min.weight;

		target[min.a].push_back(ea);
		target[min.b].push_back(eb);

		if(!as)
			for(const auto& edge : source.at(min.a))
				if(!edge.found_in(blacklist))
					if(target.find(edge.b) == target.end())
						queue.push(edge);
		if(!bs)
			for(const auto& edge : source.at(min.b))
				if(!edge.found_in(blacklist))
					if(target.find(edge.b) == target.end())
						queue.push(edge);
	}

	return target;
}

template<typename T>
void for_each_edge(const AdjacencyList& graph, T functor)
{
	auto seen = Blacklist{};
	for(auto iter = graph.begin(); iter != graph.end(); ++iter)
	{
		for(const auto& edge : iter->second)
		{
			if(!edge.found_in(seen))
			{
				functor(edge);
				seen.insert(edge.pair());
			}
		}
	}
}

uint64_t cost(const AdjacencyList& graph)
{
	uint64_t cost = 0;
	for_each_edge(graph, [&](Edge e) {
		cost += e.weight;
	});

	return cost;
}

void run()
{
	uint16_t nodes;
	uint16_t edges;

	std::cin >> nodes;
	std::cin >> edges;

	auto graph = AdjacencyList{};
	for(uint16_t i = 0; i < edges; ++i)
	{
		size_t a;
		size_t b;
		uint16_t weight;

		std::cin >> a;
		std::cin >> b;
		std::cin >> weight;

		a -= 1;
		b -= 1;

		auto ea = Edge{};
		ea.a = a;
		ea.b = b;
		ea.weight = weight;

		auto eb = Edge{};
		eb.a = b;
		eb.b = a;
		eb.weight = weight;

		graph[a].push_back(ea);
		graph[b].push_back(eb);
	}

	const auto minimum = mst(graph, Blacklist{});
	const auto minimum_cost = cost(minimum);

	auto second_minimum = minimum;
	uint64_t second_minimum_cost = 0;

	for_each_edge(minimum, [&](Edge e) {
		const auto blacklist = Blacklist{e.pair()};
		const auto candidate = mst(graph, blacklist);
		const auto candidate_cost = cost(candidate);

		if(second_minimum_cost == 0 || candidate_cost < second_minimum_cost)
		{
			second_minimum = candidate;
			second_minimum_cost = candidate_cost;
		}
	});

	std::cout << minimum_cost << " " << second_minimum_cost << std::endl;
}

int main()
{
	uint8_t runs;

	std::cin >> runs;
	while(runs--) run();
}