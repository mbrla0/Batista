use std::collections::HashMap;

fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let cards = input.next::<usize>();

	let mut counts = HashMap::new();
	let mut max = 0;

	for _ in 0..cards {
		let val = input.next::<u32>();
		*counts.entry(val).or_insert(0u32) += 1;

		if max < val { max = val; }
	}

	let mut conan = false;
	for i in (0..=max).rev() {
		if counts.get(&i).unwrap_or(&0) % 2 == 1 {
			conan = true;
			break;
		}
	}

	println!("{}", if conan { "Conan" } else { "Agasa" })
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