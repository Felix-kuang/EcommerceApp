import Link from "next/link";

const Navbar = () => {
  return (
    <nav className="bg-white shadow-md p-4">
      <div className="container mx-auto justify-between items-center">
        <Link href="/" className="text-xl font-bold">
          E-Commerce
        </Link>
        <div className="space-x-4">
          <Link href="/cart" className="px-4 py-2 border rounded-lg">
            Cart{" "}
          </Link>
          <Link
            href="/login"
            className="px-4 py-2 bg-blue-600 text-white rounded-lg"
          >
            Login
          </Link>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;