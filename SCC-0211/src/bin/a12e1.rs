fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let upper = input.next::<u16>();
	assert!(upper >= 1);
	assert!(upper <= 3000);

	let mut almost_primes = Vec::new();
	sieve(upper, &mut almost_primes);

	println!("{}", almost_primes.len());
}

pub fn sieve<E>(upper: u16, primes: &mut E)
	where E: Extend<u16> {

	let mut buffer = vec![0u8; usize::from(upper)];

	for i in 2..=upper {
		if buffer[(i - 2) as usize] == 0 {
			let mut index = i;
			while index <= upper {
				buffer[(index - 2) as usize] += 1;
				index += i;
			}
		}
	}

	primes.extend({
		let len = buffer.len();
		buffer.into_iter()
			.take(len - 1)
			.enumerate()
			.filter(|(_, passes)| *passes == 2)
			.map(|(number, _)| number as u16 + 2)
	})
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