use std::collections::HashMap;

fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let integers = input.next::<u64>();
	let mut occurrences = HashMap::new();

	for _ in 0..integers {
		let a = input.next::<u64>();

		let count = occurrences.entry(a)
			.or_insert_with(|| 0u64);
		*count += 1;
	}

	let mut changes = 0u128;
	for (value, occurrences) in occurrences {
		let abs_delta = if occurrences > value {
			/* We'll always be removing as many elements as there are extra,
			 * when it happens that the sequence has more elements than it
			 * should have. */
			occurrences - value
		} else {
			/* If the sequence has fewer or exactly as many elements as it
			 * should have, the number of elements we should add or remove is
			 * equal to the distance in elements to either zero or the value
			 * of the integer. */
			let left = occurrences;
			let right = value - left;

			u64::min(left, right)
		};

		changes += abs_delta as u128;
	}

	println!("{}", changes);
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