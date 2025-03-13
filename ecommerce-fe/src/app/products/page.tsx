"use client";

import { useState } from "react";
import Link from "next/link";
import Image from "next/image";

const categories = ["All", "Electronics", "Clothing", "Home", "Sports"];

const products = Array(20)
  .fill(null)
  .map((_, i) => ({
    id: i + 1,
    name: `Product ${i + 1}`,
    price: `$${(i + 1) * 10}`,
    category: categories[i % categories.length],
    image: "/placeholder.png",
  }));

const ProductsPage = () => {
  const [selectedCategory, setSelectedCategory] = useState("All");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 8;

  const filteredProducts =
    selectedCategory === "All"
      ? products
      : products.filter((p) => p.category === selectedCategory);

  const paginatedProducts = filteredProducts.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );
  return (
    <section className="p-5">
      <h1 className="text-2xl font-bold mb-5">Product List</h1>
      <div className=" flex gap-4 mb-5 overflow-auto">
        {categories.map((category) => (
          <button
            key={category}
            onClick={() => setSelectedCategory(category)}
            className="px-4 py-2 rounded-md border"
          >
            {category}
          </button>
        ))}
      </div>
      {/* Product Grid */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-5">
        {paginatedProducts.map((product) => (
          <div
            key={product.id}
            className="bg-white rounded-xl shadow-md overflow-hidden"
          >
            <Image
              src={product.image}
              alt={product.name}
              width={300}
              height={200}
              className="w-full h-48 object-cover"
            />
            <div className="p-4">
              <h3 className="text-lg font-semibold">{product.name}</h3>
              <p className="text-gray-500">{product.price}</p>
              <Link
                href={`/product/${product.id}`}
                className="mt-2 block text-blue-500 hover:underline"
              >
                View Details
              </Link>
            </div>
          </div>
        ))}
      </div>

      {/* Pagination */}
      <div className="flex justify-center mt-5 gap-2">
        {[...Array(Math.ceil(filteredProducts.length / itemsPerPage))].map(
          (_, i) => (
            <button
              key={i}
              onClick={() => setCurrentPage(i + 1)}
              className={`px-4 py-2 rounded-md ${
                currentPage === i + 1 ? "bg-blue-500 text-white" : "bg-gray-100"
              }`}
            >
              {i + 1}
            </button>
          )
        )}
      </div>
    </section>
  );
};

export default ProductsPage;
