#include <iostream>
#include <string>
#include <cstdint>

struct Prefix
{
private:
	bool    _present;
	bool    _live;
	Prefix* _children;

public:
	Prefix()
		: _present(false), _live(false), _children(nullptr)
	{ }
	~Prefix()
	{
		delete[] _children;
	}

	void insert(const std::string& string, size_t depth = 0)
	{
		_live = true;
		if(depth >= string.length())
		{
			_present = true;
			return;
		}
		if(_children == nullptr)
			_children = new Prefix[26];
		_children[string[depth] - 'a'].insert(string, depth + 1);
	}

	uint64_t count(size_t depth = 0, size_t longest_fork = 0)
	{
		if(_present)
			return longest_fork + 1;
		if(_children != nullptr)
		{
			uint64_t acc = 0;
			uint64_t live = 0;
			for(int i = 0; i < 26; ++i)
				if(_children[i]._live) ++live;

			for(int i = 0; i < 26; ++i)
			{
				if(!_children[i]._live)
					continue;

				size_t longest = live > 1 ? depth : longest_fork;
				acc += _children[i].count(depth + 1, longest);
			}
			return acc;
		}
		return 0;
	}
};

void run()
{
	uint64_t names;
	std::cin >> names;

	Prefix trie;
	while(names--)
	{
		std::string line;
		std::getline(std::cin, line);

		if(line.empty())
		{
			names++;
			continue;
		}

		trie.insert(line);
	}

	std::cout << trie.count() << std::endl;
}

int main()
{
	uint64_t runs;
	std::cin >> runs;
	while(runs--) run();
}