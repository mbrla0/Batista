fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u32>();
	for _ in 0..runs { run(&mut input) }
}

type AdjacencyList = Vec<Vec<usize>>;
type Results = Vec<Option<bool>>;

fn run<R: std::io::BufRead>(input: &mut Scanner<R>) {
	let nodes = input.next::<usize>();
	let edges = input.next::<usize>();

	let mut graph = AdjacencyList::with_capacity(nodes);
	graph.resize_with(nodes, Default::default);

	for _ in 0..edges {
		let a = input.next::<usize>() - 1;
		let b = input.next::<usize>() - 1;

		graph[a].push(b);
		graph[b].push(a);
	}

	let mut results = Results::with_capacity(nodes);
	results.resize_with(nodes, Default::default);

	drill(&graph, &mut results, 0, true);

	let lit: usize = results.iter()
		.map(|a| if a.unwrap() {
			1usize
		} else {
			0usize
		})
		.sum();

	let results = if lit <= nodes / 2 {
		results.into_iter()
			.filter(|a| a.unwrap())
			.enumerate()
			.map(|(i, _)| i)
			.collect::<Vec<_>>()
	} else {
		results.into_iter()
			.filter(|a| !a.unwrap())
			.enumerate()
			.map(|(i, _)| i)
			.collect::<Vec<_>>()
	};

	println!("{}", results.len());
	for light in results { print!("{} ", light + 1) }
	println!()
}

fn drill(
	graph: &AdjacencyList,
	results: &mut Results,
	node: usize,
	acc: bool) {

	results[node] = Some(acc);
	for connected in &graph[node] {
		if let None = results[*connected] {
			drill(graph, results, *connected, !acc)
		}
	}
}

struct Scanner<R> { source: R, buffer: std::collections::VecDeque<String> }
impl<R: std::io::BufRead> Scanner<R> {
	fn new(source: R) -> Self {
		Self { source, buffer: Default::default() }
	}
	fn next<T: std::str::FromStr>(&mut self) -> T {
		while self.buffer.len() == 0 {
			let mut line = String::new();
			let result = std::io::BufRead::read_line(&mut self.source, &mut line);
			if result.unwrap() == 0 { break }

			line.split_whitespace()
				.map(|str| str.trim().to_owned())
				.for_each(|str| self.buffer.push_back(str));
		}
		self.buffer.pop_front()
			.and_then(|str| str.parse().ok())
			.unwrap()
	}
}