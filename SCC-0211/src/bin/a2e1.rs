
fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u8>();
	for _ in 0..runs { run(&mut input); }
}

fn run<R: std::io::BufRead>(input: &mut Scanner<R>) {
	let count = input.next();
	let mut acts = Vec::with_capacity(count);
	let mut last = 0;

	for _ in 0..count {
		acts.push((
			input.next::<u32>(),
			input.next::<u32>()
		))
	}
	acts.sort_by(|a, b| a.1.cmp(&b.1));

	let mut total = 0usize;
	for activity in acts {
		if last <= activity.0 {
			last = activity.1;
			total += 1;
		}
	}

	println!("{}", total)
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