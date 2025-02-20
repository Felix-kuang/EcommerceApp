"use client"
import React, { useEffect, useState } from 'react';
import { getAllProducts } from '@/lib/productService';

export default function ProductsPage() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await getAllProducts();
        setProducts(data);
      } catch (error) {
        console.error('Failed to fetch products:', error);
      }
    };

    fetchProducts();
  }, []);

  return (
    <main className="p-4">
      <h1 className="text-3xl font-bold mb-4">Product List</h1>
      <ul className="space-y-4">
        {products.map((product: any) => (
          <li key={product.id} className="border p-4 rounded shadow-sm">
            <h2 className="text-xl font-semibold">{product.name}</h2>
            <p>{product.description}</p>
            <p className="text-green-600 font-bold">${product.price}</p>
          </li>
        ))}
      </ul>
    </main>
  );
}
