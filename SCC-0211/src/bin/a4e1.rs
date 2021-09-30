fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u32>();
	for _ in 0..runs { run(&mut input); }
}

fn run<R: std::io::BufRead>(input: &mut Scanner<R>) {
	let len = input.next();

	let mut data = vec![0u32; len];
	for data in &mut data { *data = input.next() }

	for i in &data {
		let result = drill(&data[..], *i, 0).unwrap();
		print!("{} ", result);
	}
	println!()
}

fn drill(data: &[u32], target: u32, curr_depth: usize) -> Option<usize> {
	let (max, _) = data.iter()
		.cloned()
		.enumerate()
		.max_by(|(_, a), (_, b)| a.cmp(b))?;
	let (target, _) = data.iter()
		.cloned()
		.enumerate()
		.find(|(_, a)| *a == target)?;

	if target == max { return Some(curr_depth) }
	else if target < max {
		drill(&data[..max], data[target], curr_depth + 1)
	} else {
		drill(&data[max + 1..], data[target], curr_depth + 1)
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