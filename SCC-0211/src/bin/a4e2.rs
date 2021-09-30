fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u32>();
	for _ in 0..runs { run(&mut input); }
}

fn run<R: std::io::BufRead>(input: &mut Scanner<R>) {
	let _ = input.next::<usize>();

	let word = input.next::<String>();
	let result = drill(word.as_bytes(), b'a');

	println!("{}", result);
}

fn drill(string: &[u8], good: u8) -> usize {
	if string.len() == 1 {
		return if string[0] == good { 0 } else { 1 }
	}

	/* Find the half which needs the fewest replacements to turn good. */
	let half = string.len() / 2;
	let left = &string[..half];
	let right = &string[half..];

	let left_subs = left.iter()
		.filter(|a| **a != good)
		.count();
	let right_subs = right.iter()
		.filter(|a| **a != good)
		.count();

	let a = drill(left, good + 1) + right_subs;
	let b = drill(right, good + 1) + left_subs;

	usize::min(a, b)
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