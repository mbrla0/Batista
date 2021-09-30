fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u32>();
	for _ in 0..runs { run(&mut input) }
}

fn run<R: std::io::BufRead>(input: &mut Scanner<R>) {
	let charge = i64::from(input.next::<u32>());
	let rounds = i64::from(input.next::<u32>());

	let a = i64::from(input.next::<u32>());
	let b = i64::from(input.next::<u32>());

	let awns = if i64::min(a, b) * rounds >= charge {
		-1i64
	} else {
		let mut min = 0;
		let mut max = rounds;
		let mut partial = 0;

		while min <= max {
			let mid = (min + max) / 2;

			let a = a * mid;
			let b = b * (rounds - mid);

			if a + b < charge {
				partial = mid;
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}

		partial
	};
	println!("{}", awns);
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