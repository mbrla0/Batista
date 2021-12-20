fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let _ = input.next::<usize>();
	let _ = input.next::<usize>();

	let table = Costs::from_scanner(&mut input);
	let a = input.next::<String>().chars().collect::<Vec<_>>();
	let b = input.next::<String>().chars().collect::<Vec<_>>();

	let mes = mes(&a[..], &b[..], &table);
	println!("{}", mes)
}

fn mes(a: &[char], b: &[char], table: &Costs) -> u64 {
	let mut costs = Table::new(a.len(), b.len());
	for i in 0..=a.len() {
		for j in 0..=b.len() {
			if i == 0 || j == 0 {
				*costs.get_mut(i, j) = 0u64;
			} else if a[i - 1] == b[j - 1] {
				*costs.get_mut(i, j) = *costs.get_mut(i - 1, j - 1) + table.cost(a[i - 1]) as u64;
			} else {
				*costs.get_mut(i, j) = u64::max(
					*costs.get_mut(i - 1, j),
					*costs.get_mut(i, j - 1));
			}
		}
	}

	*costs.get_mut(a.len(), b.len())
}

struct Table<T>{ width: usize, height: usize, data: Vec<T> }
impl<T> Table<T>
	where T: Default {

	pub fn new(width: usize, height: usize) -> Self {
		let mut data = Vec::new();
		data.resize_with((width + 1) * (height + 1), Default::default);

		Self{ width, height, data }
	}
	pub fn get_mut(&mut self, x: usize, y: usize) -> &mut T {
		assert!(x <= self.width);
		assert!(y <= self.height);
		&mut self.data[y * (self.width + 1) + x]
	}
}

struct Costs([u32; 26]);
impl Costs {
	pub fn from_scanner<R>(input: &mut Scanner<R>) -> Self
		where R: std::io::BufRead {

		let mut base = Self([0; 26]);
		for i in 0..base.0.len() {
			base.0[i] = input.next()
		}
		base
	}
	pub fn cost(&self, letter: char) -> u32 {
		assert!(letter.is_ascii_alphanumeric() && letter.is_ascii_lowercase());
		self.0[letter as usize - 'a' as usize]
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