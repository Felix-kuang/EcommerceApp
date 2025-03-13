"use client";
import Image from "next/image";

// Dummy Products
const products = [
  {
    id: 1,
    name: "Sneakers Limited Edition",
    price: "Rp 1.500.000",
    image: "/images/sneakers.jpeg",
  },
  {
    id: 2,
    name: "Smartwatch Series 7",
    price: "Rp 3.200.000",
    image: "/images/smartwatch.jpg",
  },
  {
    id: 3,
    name: "Wireless Earbuds Pro",
    price: "Rp 1.100.000",
    image: "/images/earbuds.jpg",
  },
  {
    id: 4,
    name: "Gaming Keyboard RGB",
    price: "Rp 850.000",
    image: "/images/keyboard.jpg",
  },
];

export default function RecommendedProducts() {
  return (
    <section className="py-10 px-5">
      <h2 className="text-2xl font-bold mb-5">Produk Rekomendasi</h2>
      <div className="grid grid-cols-2 md:grid-cols-4 gap-5">
        {products.map((product) => (
          <div
            key={product.id}
            className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition"
          >
            <Image
            
              src={product.image}
              alt={product.name}
              width={300}
              height={200}
              className="w-full h-48 object-contain"
            />
            <div className="p-4">
              <h3 className="text-lg font-semibold min-h-[100px]">{product.name}</h3>
              <p className="text-gray-500">{product.price}</p>
              <button className="mt-3 w-full bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600 transition">
                Lihat Detail
              </button>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}
