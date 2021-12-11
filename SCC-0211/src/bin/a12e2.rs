fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let gdc = {
		let count = input.next();
		let mut gdc_ = input.next::<u64>();
		for _ in 1..count {
			let integer = input.next();
			gdc_ = gdc(gdc_, integer);
		}
		gdc_
	};

	let mut divisors = 0usize;

	let max = (gdc as f64).sqrt();
	let max = if max.fract() == 0.0 { divisors += 1; max - 1.0 } else { max.floor() };

	for i in 1..=max as u64 {
		if gdc % i == 0 { divisors += 2; }
	}

	println!("{}", divisors);
}

fn gdc(mut a: u64, mut b: u64) -> u64 {
	while b != 0 {
		let t = b;
		b = a % b;
		a = t;
	}
	a
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