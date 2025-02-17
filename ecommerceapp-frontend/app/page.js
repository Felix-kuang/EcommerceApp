import { getProducts } from "@/lib/api";

export default async function Home() {
  const products = await getProducts();
  return (
    <div>
      <h1>Product list</h1>
      <ul>
        {products.map((product)=>(
          <li key={product.id}>
            <h2>{product.name}</h2>
            <p>{product.description}</p>
            <p>Price: ${product.price}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}
