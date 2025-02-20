import "./globals.css";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="bg-gray-100 text-gray-800">
        <nav className="bg-blue-600 text-white p-4 shadow-md">
          <h1 className="text-2xl font-bold">My Ecommerce App</h1>
        </nav>
        <main className="p-4">{children}</main>
        <footer className="bg-blue-600 text-white p-4 mt-8 text-center">
          © 2025 My Ecommerce
        </footer>
      </body>
    </html>
  )
}
