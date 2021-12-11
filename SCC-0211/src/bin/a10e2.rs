fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u64>();
	for _ in 0..runs { run(&mut input); }
}

fn run<R>(input: &mut Scanner<R>)
	where R: std::io::BufRead {

	let integers = {
		let count = input.next();
		let mut integers = Vec::with_capacity(count);

		for _ in 0..count {
			integers.push(input.next::<u16>());
		}

		integers
	};
	assert!(integers.len() >= 1);
	assert!(integers.len() <= 100);

	let base = 1u128 << (integers.len() as u128 - 1);
	let sum = integers.iter().fold(0u128, |a, x| a + *x as u128);

	/* The problem we're trying to solve is essentially the sum of all the
	 * elements in the power set of the integers vector, minus the value of a
	 * single pass over it.
	 *
	 * We know that, for a set `U`, comprised of `n` elements, its power set
	 * `P(U)` has `2^n` elements that, due to the nature of its construction,
	 * have the elements in `U` appear exactly `2^(n-1)` times.
	 *
	 * We can take advantage of that fact to massively speed up this
	 * computation, as all we need to do is to perform a sum between every
	 * element in the vector multiplied by `2^(n-1)`.
	 *
	 * Finally, we subtract the simple sum of the vector from the flat mapped
	 * sum of the power set.
	 */
	let acc = integers
		.iter()
		.fold(
			0u128,
			|a, x| {
				let x = *x as u128;
				a + x * base
			});

	println!("{}", acc - sum)
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