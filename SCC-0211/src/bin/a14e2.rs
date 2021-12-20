fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let entries = input.next::<usize>();
	let queries = input.next::<usize>();

	let mut radix = Radix::new();
	for _ in 0..entries { radix.insert(input.next()) }
	for _ in 0..queries {
		let query = input.next::<String>();
		let matches = radix.count_with_prefix(query);
		println!("{}", matches)
	}
}

pub struct Radix(RadixNode);
impl Radix {
	pub fn new() -> Self {
		Self(RadixNode::Middle { children: Vec::new(), instances: 0 })
	}

	fn shared_prefix<B>(a: &str, b: B) -> &str
		where B: AsRef<str> {

		let b = b.as_ref();
		let length = a.chars()
			.zip(b.chars())
			.take_while(|(a, b)| a == b)
			.map(|a| a.0)
			.fold(0usize, |a, matched| {
				let mut buffer = [0; 6];
				a + matched.encode_utf8(&mut buffer).len()
			});

		&a[..length]
	}

	fn print_tree(&self) {
		fn scan(root: &RadixNode, depth: usize, acc: &str) {
			for _ in 0..depth { print!("    ") }
			match root {
				RadixNode::Middle { children, instances } => {
					println!("{} x {}",
						if acc.len() == 0 { "<ROOT>" } else { acc },
						instances);
					for (prefix, next) in children {
						scan(next, depth + 1, &format!("{}{}", acc, prefix))
					}
				},
				RadixNode::Leaf { instances } =>
					println!("{} x {}", if acc.len() == 0 { "<ROOT>" } else { acc }, instances)
			}
		}
		println!();
		println!("--");
		scan(&self.0, 0, "");
		println!("--");
	}

	pub fn insert(&mut self, string: String) {
		assert!(!string.is_empty());
		fn find<'a>(root: &'a mut RadixNode, prefix: &str, acc: &str) -> (&'a mut RadixNode, String) {
			let b = unsafe { &mut *(root as *mut _) };
			match root {
				_ if prefix.is_empty() => (root, acc.to_owned()),
				RadixNode::Leaf { .. } => (root, acc.to_owned()),
				RadixNode::Middle { children, .. } => {
					for (candidate, next) in children {
						if prefix.starts_with(candidate.as_str()) {
							let new = &prefix[candidate.len()..];
							let acc = format!("{}{}", acc, candidate);

							return find(next, new, &acc)
						}
					}
					(b, acc.to_owned())
				}
			}
		}
		let (node, shared) = find(&mut self.0, &string, "");

		if shared == string {
			/* We don't need to actually change the tree in any way, we just
			 * need to make sure that we mark the repeats. */
			match node {
				RadixNode::Middle { instances, .. } => *instances += 1,
				RadixNode::Leaf { instances, .. } => *instances += 1,
			}
		} else {
			let suffix = &string[shared.len()..];
			let mut new_node = None;
			match node {
				RadixNode::Middle { children, .. } => {
					for (candidate, next) in children.iter_mut() {
						let shared = Self::shared_prefix(suffix, &candidate);
						if shared.len() > 0 {
							/* We have found an edge that shares a common prefix
							 * with the current suffix. We should split it. */
							let a = (
								(&suffix[shared.len()..]).to_owned(),
								Box::new(RadixNode::Leaf { instances: 1 })
							);
							let new = RadixNode::Middle {
								children: vec![a],
								instances: 0
							};

							/* Create a new edge to point to the original node. */
							let b = (
								(&candidate[shared.len()..]).to_owned(),
								std::mem::replace::<Box<RadixNode>>(next, Box::new(new))
							);

							/* Update the original node. */
							match &mut **next {
								RadixNode::Middle { children, .. } =>
									children.push(b),
								_ => unreachable!()
							}
							*candidate = shared.to_owned();

							/* We're done. */
							return
						}
					}

					/* We haven't found a single edge which shares a common
					 * prefix with the current suffix. We should, then add a
					 * whole new edge to this node. */
					children.push((suffix.to_owned(), Box::new(RadixNode::Leaf { instances: 1 })))
				}
				RadixNode::Leaf { instances } => {
					/* Give this node a child. */
					new_node = Some(RadixNode::Middle {
						children: vec![(suffix.to_owned(), Box::new(RadixNode::Leaf { instances: 1 }))],
						instances: *instances
					});
				}
			}
			if let Some(new_node) = new_node {
				*node = new_node
			}
		}
	}

	pub fn count_with_prefix<A>(&self, prefix: A) -> usize
		where A: AsRef<str> {

		let mut prefix = prefix.as_ref();
		let mut node = Some(&self.0);

		'outer: while let Some(RadixNode::Middle { children, .. }) = &node {
			for (candidate, next) in children {
				let shared = Self::shared_prefix(prefix, candidate);
				if shared.len() > 0 {
					prefix = &prefix[shared.len()..];
					node = Some(&**next);
					continue 'outer
				}
			}
			break
		}
		if prefix.len() > 0 {
			/* We have either found a valid leaf node or an intermediary node
			 * where our search has to stop. */
			return 0
		}

		fn count(base: &RadixNode) -> usize {
			match base {
				RadixNode::Middle { children, instances } =>
					children.iter()
						.map(|(_, next)| count(&*next))
						.fold(0, std::ops::Add::add)
						+ instances,
				RadixNode::Leaf { instances } => *instances
			}
		}

		count(node.unwrap())
	}
}

enum RadixNode {
	Middle {
		children: Vec<(String, Box<RadixNode>)>,
		instances: usize,
	},
	Leaf {
		instances: usize,
	},
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