fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u32>();
	for _ in 0..runs { run(&mut input); }
}

fn run<R>(input: &mut Scanner<R>)
	where R: std::io::BufRead {

	let mut rentals = {
		let count = input.next();
		let mut rentals = Vec::with_capacity(count);

		for _ in 0..count {
			let start = input.next();
			let length = input.next();
			let price = input.next();
			rentals.push(Rental { start, length, price })
		}

		rentals
	};
	rentals.sort_by(|a, b| a.finish().cmp(&b.finish()));

	let mut solutions = Vec::with_capacity(rentals.len());
	solutions.push(rentals[0].price as u64);

	for i in 1..rentals.len() {
		let latest = |rentals: &[Rental], curr: Rental|
			rentals.iter()
				.enumerate()
				.rev()
				.find(|(_, rental)| rental.finish() <= curr.start as u64)
				.map(|(i, _)| i);

		let base0 = rentals[i].price as u64;
		let base1 = latest(&rentals[..i], rentals[i])
			.map(|i| solutions[i]);

		let a = base0 + base1.unwrap_or(0);
		let b = solutions[i - 1];

		let inc = u64::max(a, b);
		solutions.push(inc);
	}

	let result = solutions.last().unwrap();
	println!("{}", result);
}

#[derive(Debug, Copy, Clone, Eq, PartialEq, Hash)]
struct Rental {
	start: u32,
	length: u32,
	price: u32,
}
impl Rental {
	fn finish(&self) -> u64 {
		self.start as u64 + self.length as u64
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