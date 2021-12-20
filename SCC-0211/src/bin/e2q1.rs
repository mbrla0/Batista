fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let mut a = input.next::<u64>();
	let mut b = input.next::<u64>();
	let mut n = input.next::<u64>();

	enum Players { A, B }
	let mut round = Players::A;

	let gdc = |mut a, mut b| { while a > 0 { let c = a; a = b % a; b = c }; b };
	while n > 0 {
		let stash = match round { Players::A => &mut a, Players::B => &mut b };

		let gdc = gdc(*stash, n);
		if gdc <= n { n -= gdc } else { break }

		round = match round { Players::A => Players::B, Players::B => Players::A }
	}

	println!(
		"{}",
		match round { Players::A => 1, Players::B => 0 })
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