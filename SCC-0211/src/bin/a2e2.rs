
fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let squares = input.next::<usize>();
	let mut squares = vec![0; squares];
	for square in &mut squares {
		*square = input.next::<i32>();
	}

	let mut sorted = squares.clone();
	sorted.sort();

	for (sorted, root) in sorted.into_iter().zip(squares) {
		if (sorted - root).abs() > 1 {
			println!("No");
			return
		}
	}

	println!("Yes")
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