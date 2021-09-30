fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let index = |n| match n {
		4 => 0usize,
		8 => 1,
		15 => 2,
		16 => 3,
		23 => 4,
		42 => 5,
		_ => panic!()
	};
	let mut progress = [0u64; 6];

	let nums = input.next::<u64>();
	for _ in 0..nums {
		let num = input.next::<u8>();
		let index = index(num);

		if index == 0 {
			progress[index] += 1;
		} else if progress[index - 1] > 0 {
			progress[index - 1] -= 1;
			progress[index] += 1;
		}
	}

	println!("{}", nums.saturating_sub(progress[5] * 6));
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