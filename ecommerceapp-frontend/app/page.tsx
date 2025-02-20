// app/page.tsx
import Link from 'next/link';

export default function Home() {
  return (
    <main className="p-4">
      <h1 className="text-3xl font-bold mb-4">Welcome to E-commerce App!</h1>
      <p>
        <Link href="/products">
          <button className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
            View Products
          </button>
        </Link>
      </p>
    </main>
  );
}
