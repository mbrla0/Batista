fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let nodes = input.next::<usize>();
	let edges = input.next::<usize>();

	let mut connections = Vec::new();
	connections.resize_with(nodes, || 0);

	for _ in 0..edges {
		let a = input.next::<usize>() - 1;
		let b = input.next::<usize>() - 1;

		connections[a] += 1;
		connections[b] += 1;
	}

	for connection in connections { println!("{}", connection) }
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