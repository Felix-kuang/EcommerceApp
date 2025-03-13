
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import "../globals.css";

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className="bg-gray-50 transition-colors duration-300 dark:bg-gray-900">
          <Navbar />
          <main className="container mx-auto p-4 min-h-screen dark:bg-gray-900 bg-white">
            {children}
          </main>
          <Footer />
      </body>
    </html>
  );
}
