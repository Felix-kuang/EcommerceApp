import type { Metadata } from "next";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import "../globals.css"

export const metadata: Metadata = {
  title: "E-Commerce App",
  description: "An awesome e-commerce platform built with Next.js",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="bg-gray-50">
        <Navbar />
        <main className="container mx-auto p-4 min-h-screen">{children}</main>
        <Footer />
      </body>
    </html>
  );
}
