fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<usize>();
	for _ in 0..runs {
		let mut n = input.next::<u64>();
		let mut m = 0u64;
		let k = input.next::<u64>();

		while n > 0 {
			let rem = n % k;
			if rem == 0 {
				n = n / k;
				m += 1;
			} else {
				n -= rem;
				m += rem;
			}
		}

		println!("{}", m);
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