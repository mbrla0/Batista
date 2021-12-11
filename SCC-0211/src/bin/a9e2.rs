fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u8>();
	for _ in 0..runs { run(&mut input); }
}

fn run<R>(input: &mut Scanner<R>)
	where R: std::io::BufRead {

	let toys = {
		let count = input.next();
		let mut toys = Vec::with_capacity(count);

		for _ in 0..count {
			let price = input.next::<u64>();
			toys.push(price);
		}

		toys
	};
	let mut aux = vec![0; toys.len()];

	println!("{}", drill(&toys[..], &mut aux[..], 0, toys.len()));
}

fn drill(data: &[u64], aux: &mut [u64], beg: usize, end: usize) -> u64 {
	if beg >= end { return 0 }
	if aux[beg] != 0 {
		return aux[beg]
	}

	if beg == end - 3 {
		aux[beg] = data[beg] + data[beg + 1] + data[beg + 2];
	} else if beg == end - 2 {
		aux[beg] = data[beg] + data[beg + 1];
	} else if beg == end - 1 {
		aux[beg] = data[beg];
	} else {
		let a = drill(data, aux, beg + 2, end) + data[beg];
		let b = drill(data, aux, beg + 4, end) + data[beg] + data[beg + 1];
		let c = drill(data, aux, beg + 6, end) + data[beg] + data[beg + 1] + data[beg + 2];

		aux[beg] = a.max(b).max(c);
	}

	aux[beg]
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